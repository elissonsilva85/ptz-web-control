package br.com.elissonsilva.ptzwebcontrol.backend.dahua.entity;

import br.com.elissonsilva.ptzwebcontrol.backend.dahua.entity.param.DahuaParamResponseLogin;
import lombok.Data;

@Data
public class DahuaResponseLogin extends DahuaResponseBase<DahuaParamResponseLogin> {

    public DahuaResponseLogin() {
        this.setParams(new DahuaParamResponseLogin());
    }

}
