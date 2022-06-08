package br.com.elissonsilva.ptzwebcontrol.backend.dahua.entity.param;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class DahuaParamRequestStartStop extends DahuaParamBase {

    private String code;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String arg1;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String arg2;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String arg3;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String arg4;

}
