package br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity.param;

import lombok.Data;

@Data
public class DahuaParamRequestFactoryInstance extends DahuaParamBase {

    private int channel = 0;

}