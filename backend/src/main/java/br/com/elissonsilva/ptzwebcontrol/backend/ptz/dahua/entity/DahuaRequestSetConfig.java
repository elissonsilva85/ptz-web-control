package br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity;

import br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity.param.DahuaParamRequestSetConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DahuaRequestSetConfig extends DahuaRequestBase<DahuaParamRequestSetConfig> {

    public DahuaRequestSetConfig() {
        this.setMethod("configManager.setConfig");
        this.setParams(new DahuaParamRequestSetConfig());
    }
}
