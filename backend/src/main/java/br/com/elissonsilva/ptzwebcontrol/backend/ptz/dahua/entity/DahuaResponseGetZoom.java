package br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity;

import br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity.param.DahuaParamResponseGetZoom;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DahuaResponseGetZoom extends DahuaResponseBase<DahuaParamResponseGetZoom> {

    public DahuaResponseGetZoom() {
        this.setParams(new DahuaParamResponseGetZoom());
    }

}
