package com.utn.restmess.request.message;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;

/**
 * Created by ignacio on 6/8/17.
 * <p>
 * Message Request.
 */
@SuppressWarnings("unused")
public class MessagePostRequest {

    @JsonProperty
    private String sender;

    @JsonProperty
    private String recipients;

    @JsonProperty
    private String subject;

    @JsonProperty
    private String content;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
