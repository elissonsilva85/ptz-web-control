package br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DahuaParamResponseGetViewRangeStatus {

    @JsonProperty("AngelH")
    private float angelH;

    @JsonProperty("AngelV")
    private float angelV;

    @JsonProperty("AzimuthH")
    private float azimuthH;

    @JsonProperty("AzimuthV")
    private float azimuthV;

    @JsonProperty("Distance")
    private float distance;

    @JsonProperty("InclinationH")
    private float inclinationH;

    @Override
    public String toString() {
        return "View Range Status {" +
                "angelH=" + angelH +
                ", angelV=" + angelV +
                ", azimuthH=" + azimuthH +
                ", azimuthV=" + azimuthV +
                ", distance=" + distance +
                ", inclinationH=" + inclinationH +
                '}';
    }
}
