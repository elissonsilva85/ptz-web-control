package br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity;

import br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity.param.DahuaParamRequestStartStop;
import lombok.Data;

@Data
public class DahuaRequestStart extends DahuaRequestBase<DahuaParamRequestStartStop> {

    public DahuaRequestStart() {
        this.setMethod("ptz.start");
        this.setParams(new DahuaParamRequestStartStop());

    }

}
