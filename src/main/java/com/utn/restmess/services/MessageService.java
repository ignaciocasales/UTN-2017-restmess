package com.utn.restmess.services;

import com.utn.restmess.entities.Message;
import com.utn.restmess.entities.User;
import com.utn.restmess.persistence.MessageRepository;
import com.utn.restmess.request.message.MessagePatchRequest;
import com.utn.restmess.request.message.MessagePostRequest;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by ignacio on 6/14/17.
 * <p>
 * MessageService class.
 */
@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserService userService;

    /**
     * Instacia y persiste mensajes enviados. Persiste dos copias,
     * una para el Receptor y otra para el usuario que lo envio.
     *
     * @param mRequest MessagePostRequest
     * @param sender   User
     * @param reciever User
     */
    private void newMessage(MessagePostRequest mRequest, User sender, User reciever) {
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

    /**
     * Devuelve lista de mensajes enviados por el usuario y ordenados
     * por fecha de creacion del mensaje.
     *
     * @param username String
     * @return List<Message>
     * @throws NoUsersException    No existe tal usuario.
     * @throws NoMessagesException El usuario no tiene mensajes.
     */
    public List<Message> getInbox(String username) throws NoUsersException,
            NoMessagesException {
        User user = userService.findByUsername(username);

        if (user == null) {
            throw new NoUsersException("Error al cargar el inbox.");
        }

        List<Message> messageList = user.getMsgList();

        if (messageList.isEmpty()) {
            throw new NoMessagesException("No hay mensajes.");
        }

        messageList.removeIf(
                value -> value.getStarred() ||
                        value.getDeleted() ||
                        value.getSender().equals(user.getUsername())
        );

        messageList = sortByDate(messageList);

        return messageList;
    }

    /**
     * Devuelve una lista de mensajes enviados por el usario y
     * ordenados por fecha de creacion del mensaje.
     *
     * @param username String
     * @return List<Message>
     * @throws NoUsersException    No existe tal usuario.
     * @throws NoMessagesException No hay mensajes enviados.
     */
    public List<Message> getSent(String username) throws NoUsersException,
            NoMessagesException {
        User user = userService.findByUsername(username);

        if (user == null) {
            throw new NoUsersException("Error al cargar mensajes enviados.");
        }

        List<Message> messageList = user.getMsgList();

        messageList.removeIf(value -> !value.getSender().equals(user.getUsername()));

        if (messageList.isEmpty()) {
            throw new NoMessagesException("No hay mensajes enviados.");
        }

        messageList = sortByDate(messageList);

        return messageList;
    }

    /**
     * Devuelve una lista de mensajes marcados como favoritos por
     * el usario y ordenados por fecha de creacion del mensaje.
     *
     * @param username String
     * @return List<Message>
     * @throws NoUsersException    No existe el usuario.
     * @throws NoMessagesException No hay mensajes favoritos.
     */
    public List<Message> getStarred(String username) throws NoUsersException,
            NoMessagesException {
        User user = userService.findByUsername(username);

        if (user == null) {
            throw new NoUsersException("Error al cargar mensajes favoritos.");
        }

        List<Message> messageList = user.getMsgList();

        messageList.removeIf(value -> !value.getStarred() && !value.getDeleted());

        if (messageList.isEmpty()) {
            throw new NoMessagesException("No hay mensajes favoritos.");
        }

        messageList = sortByDate(messageList);

        return messageList;
    }

    /**
     * Devuelve una lista de mensajes borrados por el usuario y
     * ordenados por fecha de creacion del mensaje.
     *
     * @param username String
     * @return List<Message>
     * @throws NoUsersException    No existe tal usuario.
     * @throws NoMessagesException El usuario no tiene mensajes borrados.
     */
    public List<Message> getTrashed(String username) throws NoUsersException,
            NoMessagesException {
        User user = userService.findByUsername(username);

        if (user == null) {
            throw new NoUsersException("Error al cargar mensajes eliminados.");
        }

        List<Message> messageList = user.getMsgList();

        messageList.removeIf(value -> !value.getDeleted());

        if (messageList.isEmpty()) {
            throw new NoMessagesException("No hay mensajes eliminados.");
        }

        messageList = sortByDate(messageList);

        return messageList;
    }

    /**
     * Envia un mensaje.
     *
     * @param mRequest MessagePostRequest
     * @param username String
     * @throws NoUsersException No existe tal usuario.
     */
    public void sendMessage(MessagePostRequest mRequest, String username) throws NoUsersException {
        User sender = userService.findByUsername(username);

        User recipient = userService.findByUsername(mRequest.getRecipients());

        if (sender == null || recipient == null) {
            throw new NoUsersException("Error al enviar el mensaje.");
        }

        this.newMessage(mRequest, sender, recipient);
    }

    /**
     * Actualiza un atributo de uno o mas mensajes.
     *
     * @param patchRequest MessagePatchRequest
     * @param username     String
     * @throws ForbiddenException      No tiene permisos de modificar tal mensaje.
     * @throws NotImplementedException No existe tal actualización.
     */
    public void patchMessage(MessagePatchRequest patchRequest, String username) throws ForbiddenException,
            NotImplementedException {
        User user = userService.findByUsername(username);

        List<Long> idList = patchRequest.getIdList();
        List<Message> messageList = new ArrayList<>();

        for (Long id :
                idList) {
            messageList.add(messageRepository.findOne(id));
        }

        if (messageList.isEmpty()) {
            throw new NullPointerException();
        }

        for (Message m :
                messageList) {
            if (!m.getUser().equals(user)) {
                throw new ForbiddenException("No tiene permisos para modificar este mensaje.");
            }

            switch (patchRequest.getType()) {
                case "star":
                    m.setStarred(patchRequest.getValue());
                    break;
                case "delete":
                    m.setDeleted(patchRequest.getValue());
                    break;
                default:
                    throw new NotImplementedException("No existe tal campo.");
            }
        }

        for (Message m :
                messageList) {
            messageRepository.save(m);
        }
    }

    /**
     * Ordena los mensajes por fecha.
     *
     * @param messageList List<Message>
     * @return List<Message>
     */
    private List<Message> sortByDate(List<Message> messageList) {
        messageList.sort(Collections.reverseOrder(Comparator.comparing(Message::getCreated)));

        return messageList;
    }
}
