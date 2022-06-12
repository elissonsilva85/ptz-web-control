package br.com.elissonsilva.ptzwebcontrol.backend.dahua.entity.config;

import br.com.elissonsilva.ptzwebcontrol.backend.dahua.entity.param.DahuaParamBase;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DahuaParamRequestSetConfigVideoInZoom {

    @JsonProperty("DigitalZoom")
    private boolean digitalZoom = false;

    @JsonProperty("Speed")
    private int speed;

    @JsonProperty("ZoomLimit")
    private int zoomLimit = 4;

}
