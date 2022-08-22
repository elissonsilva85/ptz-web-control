package br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity;

import br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity.param.DahuaParamRequestSetConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DahuaRequestSetTemporaryConfig<T> extends DahuaRequestBase<DahuaParamRequestSetConfig> {

    public DahuaRequestSetTemporaryConfig() {
        this.setMethod("configManager.setTemporaryConfig");
        this.setParams(new DahuaParamRequestSetConfig());
    }
}
