package com.utn.restmess.response;

import com.fasterxml.jackson.annotation.JsonProperty;

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
    private String sent;

    @JsonProperty
    private String subject;

    @JsonProperty
    private String content;

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

    public String getSent() {
        return sent;
    }

    public void setSent(String sent) {
        this.sent = sent;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
