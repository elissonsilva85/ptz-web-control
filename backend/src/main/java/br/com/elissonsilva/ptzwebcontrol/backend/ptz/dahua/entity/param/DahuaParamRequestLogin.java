package br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity.param;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DahuaParamRequestLogin extends DahuaParamBase {

    private String userName = "";
    private String password = "";
    private String clientType = "Web3.0";
    private String loginType = "Direct";

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String authorityType;

}
