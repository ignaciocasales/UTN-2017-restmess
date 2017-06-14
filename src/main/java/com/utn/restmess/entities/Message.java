package com.utn.restmess.entities;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by ignacio on 6/7/17.
 * <p>
 * Message class entity.
 */
@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "messageid", nullable = false)
    private long id;

    @Column(name = "sender", nullable = false)
    private String sender;

    @Column(name = "recipients", nullable = false)
    private String recipients;

    @Column(name = "subject", nullable = false)
    private String subject;

    @Column(name = "created_at", nullable = false)
    private Timestamp created;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "starred", nullable = false)
    private Boolean starred;

    @Column(name = "deleted", nullable = false)
    private Boolean deleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_fk", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    public Message() {
    }

    public Message(
            String sender,
            String recipients,
            String subject,
            Timestamp created,
            String content,
            Boolean starred,
            Boolean deleted
    ) {
        this.sender = sender;
        this.recipients = recipients;
        this.subject = subject;
        this.created = created;
        this.content = content;
        this.starred = starred;
        this.deleted = deleted;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipients() {
        return recipients;
    }

    public void setRecipients(String recipients) {
        this.recipients = recipients;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getStarred() {
        return starred;
    }

    public void setStarred(Boolean starred) {
        this.starred = starred;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
