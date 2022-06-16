package br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity;

import br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity.param.DahuaParamRequestKeepAlive;
import lombok.Data;

@Data
public class DahuaRequestKeepAlive extends DahuaRequestBase<DahuaParamRequestKeepAlive> {

    public DahuaRequestKeepAlive() {
        this.setMethod("global.keepAlive");
        this.setParams(new DahuaParamRequestKeepAlive());
    }

}
