package br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity;

import br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity.param.DahuaParamRequestGetZoom;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DahuaRequestGetZoom extends DahuaRequestBase<DahuaParamRequestGetZoom> {

    public DahuaRequestGetZoom() {
        this.setMethod("ptz.getZoomValue");
        this.setParams(new DahuaParamRequestGetZoom());
    }

}
