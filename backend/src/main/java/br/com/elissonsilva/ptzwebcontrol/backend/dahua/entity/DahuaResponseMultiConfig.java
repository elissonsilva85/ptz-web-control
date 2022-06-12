package br.com.elissonsilva.ptzwebcontrol.backend.dahua.entity;

import br.com.elissonsilva.ptzwebcontrol.backend.dahua.entity.param.DahuaParamResponseMultiConfig;
import br.com.elissonsilva.ptzwebcontrol.backend.dahua.entity.param.DahuaParamResponseSetConfig;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DahuaResponseMultiConfig extends DahuaResponseBase<List<DahuaResponseSetConfig>> {

    public DahuaResponseMultiConfig() {
        this.setParams(new ArrayList<>());
    }

}
