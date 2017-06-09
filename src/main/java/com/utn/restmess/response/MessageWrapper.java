package com.utn.restmess.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;

/**
 * Created by ignacio on 6/8/17.
 * <p>
 * Message Wrapper
 */
@SuppressWarnings("unused")
public class MessageWrapper {

    @JsonProperty
    private long id;

    @JsonProperty
    private String sender;

    @JsonProperty
    private String recipients;

    @JsonProperty
    private String subject;

    @JsonProperty
    private Timestamp created;

    @JsonProperty
    private String content;

    @JsonProperty
    private Boolean starred;

    @JsonProperty
    private Boolean deleted;

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
}
