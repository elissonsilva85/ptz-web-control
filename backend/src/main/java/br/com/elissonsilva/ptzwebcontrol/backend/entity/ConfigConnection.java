package br.com.elissonsilva.ptzwebcontrol.backend.entity;

import lombok.Data;

@Data
public class ConfigConnection {

    private String label;
    private String ip;
    private String user;
    private String password;

}
