package br.com.elissonsilva.ptzwebcontrol.backend.dahua.entity.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DahuaParamRequestSetConfigVideoInWhiteBalance {

    @JsonProperty("ColorTemperatureLevel")
    private int colorTemperatureLevel;

    @JsonProperty("GainBlue")
    private int gainBlue;

    @JsonProperty("GainGreen")
    private int gainGreen;

    @JsonProperty("GainRed")
    private int gainRed;

    @JsonProperty("Mode")
    private String mode;

}
