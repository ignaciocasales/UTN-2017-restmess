package com.utn.restmess.response;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by ignacio on 6/13/17.
 * <p>
 * ErrorMessageWrapper.
 */
public class ErrorMessageWrapper {

    @JsonProperty
    private String message;

    public ErrorMessageWrapper(String message) {
        setMessage(message);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
