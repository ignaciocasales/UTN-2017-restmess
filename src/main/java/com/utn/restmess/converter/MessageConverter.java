package com.utn.restmess.converter;

import com.utn.restmess.entities.Message;
import com.utn.restmess.response.MessageWrapper;
import org.joda.time.DateTime;
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

        DateTime dateTime = new DateTime(message.getCreated());
        m.setSent(
                dateTime.getDayOfMonth() +
                        "/" + dateTime.getMonthOfYear() +
                        "/" + dateTime.getYearOfEra()
        );

        m.setContent(message.getContent());

        return m;
    }
}
