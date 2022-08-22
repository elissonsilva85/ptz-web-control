package br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity.param;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class DahuaParamResponseGetPreset extends DahuaParamBase {

    private List<DahuaParamResponseGetPresetPresets> presets;

    public DahuaParamResponseGetPreset() {
        this.presets = new ArrayList<>();
    }
}
