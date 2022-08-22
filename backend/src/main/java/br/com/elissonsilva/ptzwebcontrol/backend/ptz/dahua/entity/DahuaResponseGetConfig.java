package br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity;

import br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity.param.DahuaParamResponseGetConfig;
import lombok.Data;

@Data
public class DahuaResponseGetConfig extends DahuaResponseBase<DahuaParamResponseGetConfig> {

    public DahuaResponseGetConfig() { this.setParams(new DahuaParamResponseGetConfig()); }
}
