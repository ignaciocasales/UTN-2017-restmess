package com.utn.restmess.controllers;

import com.utn.restmess.converter.MessageConverter;
import com.utn.restmess.entities.Message;
import com.utn.restmess.request.message.MessagePatchRequest;
import com.utn.restmess.request.message.MessagePostRequest;
import com.utn.restmess.response.ErrorMessageWrapper;
import com.utn.restmess.response.MessageWrapper;
import com.utn.restmess.services.*;
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
    private MessageConverter messageConverter;

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
            List<Message> messageList = messageService.getSent(username);

            return new ResponseEntity<>(this.convertList(messageList), HttpStatus.OK);
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
            List<Message> messageList = messageService.getStarred(username);

            return new ResponseEntity<>(this.convertList(messageList), HttpStatus.OK);
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
            List<Message> messageList = messageService.getTrashed(username);

            return new ResponseEntity<>(this.convertList(messageList), HttpStatus.OK);
        } catch (NoUsersException | NoMessagesException e) {
            return new ResponseEntity<>(new ErrorMessageWrapper(e.getMessage()), HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorMessageWrapper(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(
            value = "/messages",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public @ResponseBody
    ResponseEntity send(@RequestBody MessagePostRequest mRequest, @RequestHeader("user") String username) {
        try {
            messageService.sendMessage(mRequest, username);

            return new ResponseEntity(HttpStatus.CREATED);
        } catch (NoUsersException e) {
            return new ResponseEntity<>(new ErrorMessageWrapper(e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorMessageWrapper(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(
            value = "/messages",
            method = RequestMethod.PATCH,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public @ResponseBody
    ResponseEntity patch(
            @RequestBody MessagePatchRequest patchRequest,
            @RequestHeader("user") String username
    ) {
        try {
            messageService.patchMessage(patchRequest, username);

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
