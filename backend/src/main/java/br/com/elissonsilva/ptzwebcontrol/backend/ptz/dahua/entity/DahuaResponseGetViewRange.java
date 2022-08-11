package br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity;

import br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity.param.DahuaParamResponseGetViewRange;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DahuaResponseGetViewRange extends DahuaResponseBase<DahuaParamResponseGetViewRange> {

    public DahuaResponseGetViewRange() {
        this.setParams(new DahuaParamResponseGetViewRange());
    }

}
