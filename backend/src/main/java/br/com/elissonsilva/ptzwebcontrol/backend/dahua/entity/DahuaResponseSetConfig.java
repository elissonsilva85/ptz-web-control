package br.com.elissonsilva.ptzwebcontrol.backend.dahua.entity;

import br.com.elissonsilva.ptzwebcontrol.backend.dahua.entity.param.DahuaParamResponseLogin;
import br.com.elissonsilva.ptzwebcontrol.backend.dahua.entity.param.DahuaParamResponseSetConfig;
import lombok.Data;

@Data
public class DahuaResponseSetConfig extends DahuaResponseBase<DahuaParamResponseSetConfig> {

    public DahuaResponseSetConfig() {
        this.setParams(new DahuaParamResponseSetConfig());
    }

}
