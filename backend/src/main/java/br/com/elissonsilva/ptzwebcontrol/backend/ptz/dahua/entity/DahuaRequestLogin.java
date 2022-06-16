package br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity;

import br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity.param.DahuaParamRequestLogin;
import lombok.Data;

@Data
public class DahuaRequestLogin extends DahuaRequestBase<DahuaParamRequestLogin> {

    public DahuaRequestLogin() {
        this.setMethod("global.login");
        this.setParams(new DahuaParamRequestLogin());
    }
}
