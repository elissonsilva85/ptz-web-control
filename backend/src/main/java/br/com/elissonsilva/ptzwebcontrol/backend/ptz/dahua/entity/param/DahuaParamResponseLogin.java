package br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DahuaParamResponseLogin extends DahuaParamBase {

    @JsonProperty("authorization")
    private String authorization;

    @JsonProperty("encryption")
    private String encryption;

    @JsonProperty("mac")
    private String mac;

    @JsonProperty("random")
    private String random;

    @JsonProperty("realm")
    private String realm;

    @JsonProperty("keepAliveInterval")
    private int keepAliveInterval;

}
