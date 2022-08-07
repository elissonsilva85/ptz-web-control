package br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity.param;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DahuaParamRequestStartStop extends DahuaParamBase {

    private String code;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int arg1;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int arg2;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int arg3;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int arg4;

}
