package br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity.param;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DahuaParamRequestGetPreset extends DahuaParamBase {

    private int channel = 0;

}
