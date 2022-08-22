package br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity;

import br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity.param.DahuaParamRequestGetConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DahuaRequestGetConfig extends DahuaRequestBase<DahuaParamRequestGetConfig> {

    public DahuaRequestGetConfig() {
        this.setMethod("configManager.getConfig");
        this.setParams(new DahuaParamRequestGetConfig());
    }
}
