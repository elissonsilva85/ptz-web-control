package br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity;

import br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity.param.DahuaParamRequestFactoryInstance;
import lombok.Data;

@Data
public class DahuaRequestFactoryInstance extends DahuaRequestBase<DahuaParamRequestFactoryInstance> {

    public DahuaRequestFactoryInstance() {
        this.setMethod("ptz.factory.instance");
        this.setParams(new DahuaParamRequestFactoryInstance());
    }

}
