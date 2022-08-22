package br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity.param;

import br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity.config.DahuaParamConfigAbstract;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class DahuaParamResponseGetConfig<T extends DahuaParamConfigAbstract & List<DahuaParamConfigAbstract>> extends DahuaParamBase {

    private List<T> table;

}
