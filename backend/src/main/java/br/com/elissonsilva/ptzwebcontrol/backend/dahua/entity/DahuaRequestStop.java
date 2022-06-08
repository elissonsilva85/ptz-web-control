package br.com.elissonsilva.ptzwebcontrol.backend.dahua.entity;

import br.com.elissonsilva.ptzwebcontrol.backend.dahua.entity.param.DahuaParamRequestStartStop;
import lombok.Data;

@Data
public class DahuaRequestStop extends DahuaRequestBase<DahuaParamRequestStartStop> {

    public DahuaRequestStop() {
        this.setMethod("ptz.stop");
        this.setParams(new DahuaParamRequestStartStop());

    }

}
