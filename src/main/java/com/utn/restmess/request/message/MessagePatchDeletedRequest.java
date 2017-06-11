package com.utn.restmess.request.message;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by ignacio on 6/10/17.
 */
public class MessagePatchDeletedRequest {

    @JsonProperty
    private Boolean deleted;

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
