package com.utn.restmess.controllers;

import com.utn.restmess.converter.MessageConverter;
import com.utn.restmess.entities.Message;
import com.utn.restmess.persistence.MessageRepository;
import com.utn.restmess.response.MessageWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ignacio on 6/7/17.
 * <p>
 * MessageController class.
 */
@SuppressWarnings("unused")
@RestController
@RequestMapping(path = "/api")
public class MessageController {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private MessageConverter messageConverter;

    @RequestMapping(value = "/messages/{username}", method = RequestMethod.GET)
    public void inbox() {
        //TODO
    }

    @RequestMapping(value = "/messages/{username}/sent", method = RequestMethod.GET)
    public void sent() {
        //TODO
    }

    @RequestMapping(value = "/messages/{username}/starred", method = RequestMethod.GET)
    public void starred() {
        //TODO
    }

    @RequestMapping(value = "/messages/{username}/trash", method = RequestMethod.GET)
    public void trash() {
        //TODO
    }

    @RequestMapping(value = "/messages", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void send() {
        //TODO
    }

    @RequestMapping(value = "/messages/{id}", method = RequestMethod.PATCH)
    public void delete() {
        //TODO
    }

    private List<MessageWrapper> convertList(List<Message> message) {
        List<MessageWrapper> messageWrapperArrayList = new ArrayList<>();
        for (Message m : message) {
            messageWrapperArrayList.add(messageConverter.convert(m));
        }
        return messageWrapperArrayList;
    }
}
