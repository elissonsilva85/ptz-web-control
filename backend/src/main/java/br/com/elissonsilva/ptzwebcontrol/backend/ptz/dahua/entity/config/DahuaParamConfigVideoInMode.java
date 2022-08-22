package br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Data
public class DahuaParamConfigVideoInMode extends DahuaParamConfigAbstract {

    @JsonProperty("Config")
    private int[] config;

    @JsonProperty("Mode")
    private int mode = 0;

    @JsonProperty("TimeSection")
    private List<List<String>> timeSection;

    public DahuaParamConfigVideoInMode() {
        this.timeSection = Arrays.asList(
                Arrays.asList(
                        "0 00:00:00-24:00:00",
                        "0 00:00:00-24:00:00",
                        "0 00:00:00-24:00:00",
                        "0 00:00:00-24:00:00",
                        "0 00:00:00-24:00:00",
                        "0 00:00:00-24:00:00"
                ),
                Arrays.asList(
                        "0 00:00:00-24:00:00",
                        "0 00:00:00-24:00:00",
                        "0 00:00:00-24:00:00",
                        "0 00:00:00-24:00:00",
                        "0 00:00:00-24:00:00",
                        "0 00:00:00-24:00:00"
                ),
                Arrays.asList(
                        "0 00:00:00-24:00:00",
                        "0 00:00:00-24:00:00",
                        "0 00:00:00-24:00:00",
                        "0 00:00:00-24:00:00",
                        "0 00:00:00-24:00:00",
                        "0 00:00:00-24:00:00"
                ),
                Arrays.asList(
                        "0 00:00:00-24:00:00",
                        "0 00:00:00-24:00:00",
                        "0 00:00:00-24:00:00",
                        "0 00:00:00-24:00:00",
                        "0 00:00:00-24:00:00",
                        "0 00:00:00-24:00:00"
                ),
                Arrays.asList(
                        "0 00:00:00-24:00:00",
                        "0 00:00:00-24:00:00",
                        "0 00:00:00-24:00:00",
                        "0 00:00:00-24:00:00",
                        "0 00:00:00-24:00:00",
                        "0 00:00:00-24:00:00"
                ),
                Arrays.asList(
                        "0 00:00:00-24:00:00",
                        "0 00:00:00-24:00:00",
                        "0 00:00:00-24:00:00",
                        "0 00:00:00-24:00:00",
                        "0 00:00:00-24:00:00",
                        "0 00:00:00-24:00:00"
                ),
                Arrays.asList(
                        "0 00:00:00-24:00:00",
                        "0 00:00:00-24:00:00",
                        "0 00:00:00-24:00:00",
                        "0 00:00:00-24:00:00",
                        "0 00:00:00-24:00:00",
                        "0 00:00:00-24:00:00"
                )
        );
    }
}
