package com.utn.restmess.controllers;

import com.utn.restmess.converter.MessageConverter;
import com.utn.restmess.entities.Message;
import com.utn.restmess.entities.User;
import com.utn.restmess.persistence.MessageRepository;
import com.utn.restmess.persistence.UserRepository;
import com.utn.restmess.request.message.MessagePatchRequest;
import com.utn.restmess.request.message.MessagePostRequest;
import com.utn.restmess.response.MessageWrapper;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
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

    @RequestMapping(
            value = "/messages",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public @ResponseBody
    ResponseEntity<List<MessageWrapper>> inbox(@RequestHeader("user") String username) {
        try {
            User user = userRepository.findByUsername(username);

            if (user == null) {
                throw new NullPointerException();
            }

            List<Message> messageList = user.getMsgList();

            messageList.removeIf(
                    value -> value.getStarred() ||
                            value.getDeleted() ||
                            value.getSender().equals(user.getUsername())
            );

            if (messageList.size() > 0) {
                return new ResponseEntity<>(this.convertList(messageList), HttpStatus.OK);
            } else {
                throw new NullPointerException();
            }
        } catch (NullPointerException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @RequestMapping(
            value = "/messages/sent",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public @ResponseBody
    ResponseEntity<List<MessageWrapper>> sent(@RequestHeader("user") String username) {
        try {
            User user = userRepository.findByUsername(username);

            if (user == null) {
                throw new NullPointerException();
            }

            List<Message> messageList = user.getMsgList();

            messageList.removeIf(value -> !value.getSender().equals(user.getUsername()));

            if (messageList.size() > 0) {
                return new ResponseEntity<>(this.convertList(messageList), HttpStatus.OK);
            } else {
                throw new NullPointerException();
            }
        } catch (NullPointerException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(
            value = "/messages/starred",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public @ResponseBody
    ResponseEntity<List<MessageWrapper>> starred(@RequestHeader("user") String username) {
        try {
            User user = userRepository.findByUsername(username);

            if (user == null) {
                throw new NullPointerException();
            }

            List<Message> messageList = user.getMsgList();

            messageList.removeIf(value -> !value.getStarred() && !value.getDeleted());

            if (messageList.size() > 0) {
                return new ResponseEntity<>(this.convertList(messageList), HttpStatus.OK);
            } else {
                throw new NullPointerException();
            }
        } catch (NullPointerException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(
            value = "/messages/trashed",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public @ResponseBody
    ResponseEntity<List<MessageWrapper>> trash(@RequestHeader("user") String username) {
        try {
            User user = userRepository.findByUsername(username);

            if (user == null) {
                throw new NullPointerException();
            }

            List<Message> messageList = user.getMsgList();

            messageList.removeIf(value -> !value.getDeleted());

            if (messageList.size() > 0) {
                return new ResponseEntity<>(this.convertList(messageList), HttpStatus.OK);
            } else {
                throw new NullPointerException();
            }
        } catch (NullPointerException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
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
                throw new NullPointerException();
            }

            Message mSender = new Message();

            mSender.setSender(mRequest.getSender());
            mSender.setRecipients(mRequest.getRecipients());
            mSender.setSubject(mRequest.getSubject());
            mSender.setCreated(new Timestamp(DateTime.now().getMillis()));
            mSender.setContent(mRequest.getContent());
            mSender.setStarred(false);
            mSender.setDeleted(false);
            mSender.setUser(sender);

            Message mRecipient = new Message();

            mRecipient.setSender(mSender.getSender());
            mRecipient.setRecipients(mSender.getRecipients());
            mRecipient.setSubject(mSender.getSubject());
            mRecipient.setCreated(mSender.getCreated());
            mRecipient.setContent(mSender.getContent());
            mRecipient.setStarred(false);
            mRecipient.setDeleted(false);
            mRecipient.setUser(recipient);

            messageRepository.save(mSender);

            messageRepository.save(mRecipient);

            return new ResponseEntity(HttpStatus.CREATED);
        } catch (NullPointerException e) {
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.NOT_FOUND);
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
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

            return new ResponseEntity(HttpStatus.ACCEPTED);
        } catch (ForbiddenException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } catch (NotImplementedException e) {
            return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
        } catch (NullPointerException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
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
