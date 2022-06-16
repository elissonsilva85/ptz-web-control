package br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity;

import br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity.param.DahuaParamRequestSetConfig;
import lombok.Data;

@Data
public class DahuaRequestSetConfig<T> extends DahuaRequestBase<DahuaParamRequestSetConfig<T>> {

    public DahuaRequestSetConfig() {
        this.setMethod("configManager.setConfig");
        this.setParams(new DahuaParamRequestSetConfig<T>());
    }
}
