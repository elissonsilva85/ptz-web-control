package br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity;

import br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity.param.DahuaParamRequestGetPreset;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DahuaRequestGetPreset extends DahuaRequestBase<DahuaParamRequestGetPreset> {

    public DahuaRequestGetPreset() {
        this.setMethod("ptz.getPresets");
        this.setParams(new DahuaParamRequestGetPreset());
    }

}
