package br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity;

import br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity.param.DahuaParamRequestSetPreset;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DahuaRequestSetPreset extends DahuaRequestBase<DahuaParamRequestSetPreset> {

    public DahuaRequestSetPreset() {
        this.setMethod("ptz.setPreset");
        this.setParams(new DahuaParamRequestSetPreset());
    }

}
