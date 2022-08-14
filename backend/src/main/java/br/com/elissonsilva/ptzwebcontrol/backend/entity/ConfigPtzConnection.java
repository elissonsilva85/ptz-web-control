package br.com.elissonsilva.ptzwebcontrol.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class ConfigPtzConnection {

    private String brand;

    private String label;

    private int udpPort;

    @JsonIgnore
    private String url;

    @JsonIgnore
    private String user;

    @JsonIgnore
    private String password;

}
