package br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity;

import br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity.param.DahuaParamResponseStartStop;
import lombok.Data;

@Data
public class DahuaResponseStartStop extends DahuaResponseBase<DahuaParamResponseStartStop> {

    public DahuaResponseStartStop() {
        this.setParams(new DahuaParamResponseStartStop());
    }

}
