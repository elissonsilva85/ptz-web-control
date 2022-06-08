package br.com.elissonsilva.ptzwebcontrol.backend.dahua.entity;

import br.com.elissonsilva.ptzwebcontrol.backend.dahua.entity.param.DahuaParamBase;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public abstract class DahuaResponseBase<T extends DahuaParamBase> {

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
