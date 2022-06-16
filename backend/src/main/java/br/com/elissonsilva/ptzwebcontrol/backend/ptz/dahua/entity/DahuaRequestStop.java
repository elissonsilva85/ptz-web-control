package br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity;

import br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity.param.DahuaParamRequestStartStop;
import lombok.Data;

@Data
public class DahuaRequestStop extends DahuaRequestBase<DahuaParamRequestStartStop> {

    public DahuaRequestStop() {
        this.setMethod("ptz.stop");
        this.setParams(new DahuaParamRequestStartStop());

    }

}
