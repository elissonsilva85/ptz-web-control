package br.com.elissonsilva.ptzwebcontrol.backend.entity;

import lombok.Data;

import java.util.Map;

@Data
public class ConfigPtz {

    private String brand;
    private Map<String, ConfigPtzConnection> connection;

}
