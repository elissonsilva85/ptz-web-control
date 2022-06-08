package br.com.elissonsilva.ptzwebcontrol.backend.dahua.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public class DahuaResponseError {

    @JsonProperty("code")
    private int code;

    @JsonProperty("message")
    private String message;

}
