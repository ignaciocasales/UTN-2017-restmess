package com.utn.restmess.converter;

import com.utn.restmess.entities.Message;
import com.utn.restmess.response.MessageWrapper;
import org.springframework.stereotype.Component;

/**
 * Created by ignacio on 6/8/17.
 * <p>
 * Message Converter
 */
@Component
public class MessageConverter {

    public MessageConverter() {
    }

    public MessageWrapper convert(Message message) {
        MessageWrapper m = new MessageWrapper();

        m.setId(message.getId());
        m.setSender(message.getSender());
        m.setRecipients(message.getRecipients());
        m.setSubject(message.getSubject());
        m.setCreated(message.getCreated());
        m.setContent(message.getContent());
        m.setStarred(message.getStarred());
        m.setDeleted(message.getDeleted());

        return m;
    }
}
