package br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity;

import br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity.param.DahuaParamRequestGetViewRange;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DahuaRequestGetViewRange extends DahuaRequestBase<DahuaParamRequestGetViewRange> {

    public DahuaRequestGetViewRange() {
        this.setMethod("ptz.getViewRangeStatus");
        this.setParams(new DahuaParamRequestGetViewRange());
    }

}
