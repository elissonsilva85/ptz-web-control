package br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity.config;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = DahuaParamConfigVideoColorTable.class, name = "VideoColorTable"),
        @JsonSubTypes.Type(value = DahuaParamConfigVideoInFocus.class, name = "VideoInFocus"),
        @JsonSubTypes.Type(value = DahuaParamConfigVideoInMode.class, name = "VideoInMode"),
        @JsonSubTypes.Type(value = DahuaParamConfigVideoInWhiteBalance.class, name = "VideoInWhiteBalance"),
        @JsonSubTypes.Type(value = DahuaParamConfigVideoInZoom.class, name = "VideoInZoom")}
)
public abstract class DahuaParamConfigAbstract {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
