package com.utn.restmess.Services;

import com.utn.restmess.entities.Message;
import com.utn.restmess.entities.User;
import com.utn.restmess.persistence.MessageRepository;
import com.utn.restmess.request.message.MessagePostRequest;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

/**
 * Created by ignacio on 6/14/17.
 * <p>
 * MessageService class.
 */
@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public void newMessage(MessagePostRequest mRequest, User sender, User reciever) {
        Message mSender = new Message();

        mSender.setSender(sender.getUsername());
        mSender.setRecipients(mRequest.getRecipients());
        mSender.setSubject(mRequest.getSubject());
        mSender.setCreated(new Timestamp(DateTime.now().getMillis()));
        mSender.setContent(mRequest.getContent());
        mSender.setStarred(false);
        mSender.setDeleted(false);
        mSender.setUser(sender);

        Message mReciever = new Message();

        mReciever.setSender(sender.getUsername());
        mReciever.setRecipients(mRequest.getRecipients());
        mReciever.setSubject(mRequest.getSubject());
        mReciever.setCreated(new Timestamp(DateTime.now().getMillis()));
        mReciever.setContent(mRequest.getContent());
        mReciever.setStarred(false);
        mReciever.setDeleted(false);
        mReciever.setUser(reciever);

        messageRepository.save(mSender);
        messageRepository.save(mReciever);
    }
}
