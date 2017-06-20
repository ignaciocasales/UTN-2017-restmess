package com.utn.restmess.request.message;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by ignacio on 6/11/17.
 * <p>
 * MessagePatchRequest
 */
public class MessagePatchRequest {

    @JsonProperty
    private String type;

    @JsonProperty
    private List<Long> idList;

    @JsonProperty
    private Boolean value;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Long> getIdList() {
        return idList;
    }

    public void setIdList(List<Long> idList) {
        this.idList = idList;
    }

    public Boolean getValue() {
        return value;
    }

    public void setValue(Boolean value) {
        this.value = value;
    }
}
