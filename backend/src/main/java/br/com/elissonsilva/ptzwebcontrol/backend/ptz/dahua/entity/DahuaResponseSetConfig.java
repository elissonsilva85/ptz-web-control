package br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity;

import br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity.param.DahuaParamResponseSetConfig;
import lombok.Data;

@Data
public class DahuaResponseSetConfig extends DahuaResponseBase<DahuaParamResponseSetConfig> {

    public DahuaResponseSetConfig() {
        this.setParams(new DahuaParamResponseSetConfig());
    }

}
