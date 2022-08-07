package br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity;

import br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity.param.DahuaParamResponseSetTemporaryConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DahuaResponseSetTemporaryConfig extends DahuaResponseBase<DahuaParamResponseSetTemporaryConfig> {

    public DahuaResponseSetTemporaryConfig() {
        this.setParams(new DahuaParamResponseSetTemporaryConfig());
    }

}
