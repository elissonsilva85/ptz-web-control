package br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity.param;

import lombok.Data;

@Data
public class DahuaParamRequestKeepAlive extends DahuaParamBase {

    private int timeout = 1000;
    private boolean active = true;

}
