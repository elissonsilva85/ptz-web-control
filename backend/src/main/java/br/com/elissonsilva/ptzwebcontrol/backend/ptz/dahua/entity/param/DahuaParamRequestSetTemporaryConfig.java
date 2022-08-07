package br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity.param;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class DahuaParamRequestSetTemporaryConfig<T> extends DahuaParamBase {

    private String name = "";
    private List<T> table = new ArrayList<>();
    private List<String> options = new ArrayList<>();

}
