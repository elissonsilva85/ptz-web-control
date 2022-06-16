package br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity;

import br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity.param.DahuaParamResponseKeepAlive;
import lombok.Data;

@Data
public class DahuaResponseKeepAlive extends DahuaResponseBase<DahuaParamResponseKeepAlive> {

    public DahuaResponseKeepAlive() {
        this.setParams(new DahuaParamResponseKeepAlive());
    }

}
