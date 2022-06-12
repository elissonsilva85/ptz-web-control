package br.com.elissonsilva.ptzwebcontrol.backend.dahua.entity;

import br.com.elissonsilva.ptzwebcontrol.backend.dahua.entity.param.DahuaParamRequestSetConfig;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DahuaRequestMultiConfig extends DahuaRequestBase<List<DahuaRequestBase>> {

    public DahuaRequestMultiConfig() {
        this.setMethod("system.multicall");
        this.setParams(new ArrayList<>());
    }
}
