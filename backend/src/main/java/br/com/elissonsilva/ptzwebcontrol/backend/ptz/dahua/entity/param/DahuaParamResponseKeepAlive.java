package br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity.param;

import lombok.Data;

@Data
public class DahuaParamResponseKeepAlive extends DahuaParamBase {

    private int timeout;

}
