package br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity.param;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DahuaParamRequestSetConfig<T> extends DahuaParamBase {

    private String name = "";
    private List<T> table = new ArrayList<>();
    private List<String> options = new ArrayList<>();

}
