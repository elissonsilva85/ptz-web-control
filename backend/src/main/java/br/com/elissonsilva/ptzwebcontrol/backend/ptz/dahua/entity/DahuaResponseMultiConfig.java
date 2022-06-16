package br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DahuaResponseMultiConfig extends DahuaResponseBase<List<DahuaResponseSetConfig>> {

    public DahuaResponseMultiConfig() {
        this.setParams(new ArrayList<>());
    }

}
