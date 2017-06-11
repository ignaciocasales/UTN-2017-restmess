package com.utn.restmess.request.message;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by ignacio on 6/10/17.
 */
public class MessagePatchStarredRequest {

    @JsonProperty
    private Boolean starred;

    public Boolean getStarred() {
        return starred;
    }

    public void setStarred(Boolean starred) {
        this.starred = starred;
    }
}
