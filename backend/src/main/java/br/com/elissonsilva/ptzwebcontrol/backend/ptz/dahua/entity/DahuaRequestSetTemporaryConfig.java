package br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity;

import br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity.param.DahuaParamRequestSetTemporaryConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DahuaRequestSetTemporaryConfig<T> extends DahuaRequestBase<DahuaParamRequestSetTemporaryConfig<T>> {

    public DahuaRequestSetTemporaryConfig() {
        this.setMethod("configManager.setTemporaryConfig");
        this.setParams(new DahuaParamRequestSetTemporaryConfig<>());
    }
}
