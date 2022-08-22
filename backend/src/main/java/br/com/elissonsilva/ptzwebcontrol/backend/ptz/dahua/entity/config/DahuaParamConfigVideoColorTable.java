package br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DahuaParamConfigVideoColorTable extends DahuaParamConfigAbstract {

    @JsonProperty("Brightness")
    int brightness = 50;

    @JsonProperty("ChromaSuppress")
    int chromaSuppress = 50;

    @JsonProperty("Contrast")
    int contrast = 50;

    @JsonProperty("Gamma")
    int gamma = 50;

    @JsonProperty("Hue")
    int hue = 50;

    @JsonProperty("Saturation")
    int saturation = 50;

    @JsonProperty("Style")
    String style = "Standard";

    @JsonProperty("TimeSection")
    String timeSection = "0 00:00:00-24:00:00";
}
