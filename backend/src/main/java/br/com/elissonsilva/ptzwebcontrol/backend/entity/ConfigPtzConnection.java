package br.com.elissonsilva.ptzwebcontrol.backend.entity;

import lombok.Data;

@Data
public class ConfigPtzConnection {

    private String label;
    private String url;
    private String user;
    private String password;

}
