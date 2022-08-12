package br.com.elissonsilva.ptzwebcontrol.backend.entity;

import lombok.Data;

@Data
public class ConfigPtzConnection {

    private String brand;
    private int udpPort;
    private String label;
    private String url;
    private String user;
    private String password;

}
