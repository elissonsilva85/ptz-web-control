package br.com.elissonsilva.ptzwebcontrol.backend.dahua.entity;

import br.com.elissonsilva.ptzwebcontrol.backend.dahua.entity.param.DahuaParamResponseFactoryInstance;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DahuaResponseFactoryInstance extends DahuaResponseBase<DahuaParamResponseFactoryInstance> {

    @JsonProperty("result")
    private int result;

    public DahuaResponseFactoryInstance() {
        this.setParams(new DahuaParamResponseFactoryInstance());
    }

}
