package br.com.elissonsilva.ptzwebcontrol.backend.entity;

import lombok.Data;

import java.util.List;

@Data
public class ConfigShortcut {

    private String key;
    private List<String> action;

}
