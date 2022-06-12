package br.com.elissonsilva.ptzwebcontrol.backend.dahua.entity.param;

import lombok.Data;

@Data
public class DahuaParamResponseKeepAlive extends DahuaParamBase {

    private int timeout;

}
