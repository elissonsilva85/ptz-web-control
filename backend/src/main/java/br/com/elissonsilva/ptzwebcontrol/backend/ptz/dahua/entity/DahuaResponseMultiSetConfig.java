package br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DahuaResponseMultiSetConfig extends DahuaResponseBase<List<DahuaResponseSetConfig>> {

    public DahuaResponseMultiSetConfig() {
        this.setParams(new ArrayList<>());
    }

}
