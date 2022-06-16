package br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DahuaParamRequestSetConfigVideoInZoom {

    @JsonProperty("DigitalZoom")
    private boolean digitalZoom = false;

    @JsonProperty("Speed")
    private int speed;

    @JsonProperty("ZoomLimit")
    private int zoomLimit = 4;

}
