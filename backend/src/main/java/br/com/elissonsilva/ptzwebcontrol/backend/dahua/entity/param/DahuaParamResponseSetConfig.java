package br.com.elissonsilva.ptzwebcontrol.backend.dahua.entity.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DahuaParamResponseSetConfig extends DahuaParamBase {

    private String options;

}
