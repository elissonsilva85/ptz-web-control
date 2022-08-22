package br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DahuaResponseMultiGetConfig extends DahuaResponseBase<List<DahuaResponseGetConfig>> {

    public DahuaResponseMultiGetConfig() {
        this.setParams(new ArrayList<>());
    }

}
