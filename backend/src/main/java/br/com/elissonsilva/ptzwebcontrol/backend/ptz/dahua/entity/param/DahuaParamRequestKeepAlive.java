package br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity.param;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DahuaParamRequestKeepAlive extends DahuaParamBase {

    private int timeout = 1000;
    private boolean active = true;

}
