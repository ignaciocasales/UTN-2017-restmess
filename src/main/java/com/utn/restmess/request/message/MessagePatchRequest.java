package com.utn.restmess.request.message;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by ignacio on 6/11/17.
 * <p>
 * MessagePatchRequest
 */
public class MessagePatchRequest {

    @JsonProperty
    protected String type;

    @JsonProperty
    protected Boolean value;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getValue() {
        return value;
    }

    public void setValue(Boolean value) {
        this.value = value;
    }
}
