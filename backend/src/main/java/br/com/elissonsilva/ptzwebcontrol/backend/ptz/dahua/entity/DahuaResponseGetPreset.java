package br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity;

import br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity.param.DahuaParamResponseGetPreset;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DahuaResponseGetPreset extends DahuaResponseBase<DahuaParamResponseGetPreset> {

    public DahuaResponseGetPreset() {
        this.setParams(new DahuaParamResponseGetPreset());
    }

}
