package br.com.elissonsilva.ptzwebcontrol.backend.entity;

import lombok.Data;

@Data
public class SpecificPositionRequest {

    private int horizontal;
    private int vertical;
    private int zoom;

}
