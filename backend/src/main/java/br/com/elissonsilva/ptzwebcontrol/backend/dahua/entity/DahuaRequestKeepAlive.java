package br.com.elissonsilva.ptzwebcontrol.backend.dahua.entity;

import br.com.elissonsilva.ptzwebcontrol.backend.dahua.entity.param.DahuaParamRequestKeepAlive;
import lombok.Data;

@Data
public class DahuaRequestKeepAlive extends DahuaRequestBase<DahuaParamRequestKeepAlive> {

    public DahuaRequestKeepAlive() {
        this.setMethod("global.keepAlive");
        this.setParams(new DahuaParamRequestKeepAlive());
    }

}
