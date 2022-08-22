package br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity;

import br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity.param.DahuaParamResponseSetConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DahuaResponseSetTemporaryConfig extends DahuaResponseBase<DahuaParamResponseSetConfig> {

    public DahuaResponseSetTemporaryConfig() {
        this.setParams(new DahuaParamResponseSetConfig());
    }

}
