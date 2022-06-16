package br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public abstract class DahuaResponseBase<T> {

    @JsonProperty("id")
    private int id;

    @JsonProperty("result")
    private boolean result;

    @JsonProperty("session")
    private String session;

    @JsonProperty("error")
    private DahuaResponseError error;

    @JsonProperty("params")
    private T params;

}
