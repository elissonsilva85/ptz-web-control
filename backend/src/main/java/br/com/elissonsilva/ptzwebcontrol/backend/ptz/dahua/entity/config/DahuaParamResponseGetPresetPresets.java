package br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DahuaParamResponseGetPresetPresets {

    @JsonProperty("Index")
    private int index;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Type")
    private int type = 4;

    @Override
    public String toString() {
        return "Preset {" +
                "index=" + index +
                ", name='" + name + '\'' +
                ", type=" + type +
                '}';
    }
}
