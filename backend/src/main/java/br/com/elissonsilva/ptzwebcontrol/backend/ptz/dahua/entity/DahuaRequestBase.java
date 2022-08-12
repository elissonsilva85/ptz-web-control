package br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;

@Data
public abstract class DahuaRequestBase<T> {

    private String method;
    private T params;
    private int id;

    @JsonInclude(Include.NON_NULL)
    private String session;

    @JsonInclude(Include.NON_DEFAULT)
    private int object;

    @JsonInclude(Include.NON_DEFAULT)
    private long seq;

}
