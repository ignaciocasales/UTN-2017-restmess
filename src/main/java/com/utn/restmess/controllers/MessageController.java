package com.utn.restmess.controllers;

import com.utn.restmess.converter.MessageConverter;
import com.utn.restmess.entities.Message;
import com.utn.restmess.entities.User;
import com.utn.restmess.persistence.MessageRepository;
import com.utn.restmess.persistence.UserRepository;
import com.utn.restmess.request.message.MessagePatchRequest;
import com.utn.restmess.request.message.MessagePostRequest;
import com.utn.restmess.response.ErrorMessageWrapper;
import com.utn.restmess.response.MessageWrapper;
import com.utn.restmess.services.MessageService;
import com.utn.restmess.services.NoMessagesException;
import com.utn.restmess.services.NoUsersException;
import com.utn.restmess.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ignacio on 6/7/17.
 * <p>
 * MessageController class.
 */
@RestController
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class MessageController {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageConverter messageConverter;

    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;

    @RequestMapping(
            value = "/messages",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public @ResponseBody
    ResponseEntity inbox(@RequestHeader("user") String username) {
        try {
            List<Message> messageList = messageService.getInbox(username);

            return new ResponseEntity<>(this.convertList(messageList), HttpStatus.OK);
        } catch (NoUsersException | NoMessagesException e) {
            return new ResponseEntity<>(new ErrorMessageWrapper(e.getMessage()), HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorMessageWrapper(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @RequestMapping(
            value = "/messages/sent",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public @ResponseBody
    ResponseEntity sent(@RequestHeader("user") String username) {
        try {
            User user = userRepository.findByUsername(username);

            if (user == null) {
                throw new NoUsersException("Error al cargar mensajes enviados.");
            }

            List<Message> messageList = user.getMsgList();

            messageList.removeIf(value -> !value.getSender().equals(user.getUsername()));

            if (messageList.size() > 0) {
                return new ResponseEntity<>(this.convertList(messageList), HttpStatus.OK);
            } else {
                throw new NoMessagesException("No hay mensajes.");
            }
        } catch (NoUsersException | NoMessagesException e) {
            return new ResponseEntity<>(new ErrorMessageWrapper(e.getMessage()), HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorMessageWrapper(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(
            value = "/messages/starred",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public @ResponseBody
    ResponseEntity starred(@RequestHeader("user") String username) {
        try {
            User user = userRepository.findByUsername(username);

            if (user == null) {
                throw new NoUsersException("Error al cargar mensajes favoritos.");
            }

            List<Message> messageList = user.getMsgList();

            messageList.removeIf(value -> !value.getStarred() && !value.getDeleted());

            if (messageList.size() > 0) {
                return new ResponseEntity<>(this.convertList(messageList), HttpStatus.OK);
            } else {
                throw new NoMessagesException("No hay mensajes.");
            }
        } catch (NoUsersException | NoMessagesException e) {
            return new ResponseEntity<>(new ErrorMessageWrapper(e.getMessage()), HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorMessageWrapper(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(
            value = "/messages/trashed",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public @ResponseBody
    ResponseEntity trashed(@RequestHeader("user") String username) {
        try {
            User user = userRepository.findByUsername(username);

            if (user == null) {
                throw new NoUsersException("Error al cargar mensajes eliminados.");
            }

            List<Message> messageList = user.getMsgList();

            messageList.removeIf(value -> !value.getDeleted());

            if (messageList.size() > 0) {
                return new ResponseEntity<>(this.convertList(messageList), HttpStatus.OK);
            } else {
                throw new NoMessagesException("No hay mensajes.");
            }
        } catch (NoUsersException | NoMessagesException e) {
            return new ResponseEntity<>(new ErrorMessageWrapper(e.getMessage()), HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorMessageWrapper(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(
            value = "/messages",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public @ResponseBody
    ResponseEntity send(@RequestBody MessagePostRequest mRequest, @RequestHeader("user") String username) {
        try {
            User sender = userRepository.findByUsername(username);

            User recipient = userRepository.findByUsername(mRequest.getRecipients());

            if (sender == null || recipient == null) {
                throw new NoUsersException("Error al enviar el mensaje.");
            }

            messageService.newMessage(mRequest, sender, recipient);

            return new ResponseEntity(HttpStatus.CREATED);
        } catch (NoUsersException e) {
            return new ResponseEntity<>(new ErrorMessageWrapper(e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorMessageWrapper(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(
            value = "/messages/{id}",
            method = RequestMethod.PATCH,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public @ResponseBody
    ResponseEntity patch(
            @PathVariable("id") long id,
            @RequestBody MessagePatchRequest patchRequest,
            @RequestHeader("user") String username
    ) {
        try {
            User user = userRepository.findByUsername(username);

            Message m = messageRepository.findOne(id);

            if (m == null) {
                throw new NullPointerException();
            }

            if (!m.getUser().equals(user)) {
                throw new ForbiddenException();
            }

            switch (patchRequest.getType()) {
                case "star":
                    m.setStarred(patchRequest.getValue());
                    break;
                case "delete":
                    m.setDeleted(patchRequest.getValue());
                    break;
                default:
                    throw new NotImplementedException();
            }

            messageRepository.save(m);

            return new ResponseEntity(HttpStatus.ACCEPTED);
        } catch (ForbiddenException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } catch (NotImplementedException e) {
            return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
        } catch (NullPointerException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorMessageWrapper(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private List<MessageWrapper> convertList(List<Message> message) {
        List<MessageWrapper> messageWrapperArrayList = new ArrayList<>();

        for (Message m : message) {
            messageWrapperArrayList.add(messageConverter.convert(m));
        }
        return messageWrapperArrayList;
    }
}
