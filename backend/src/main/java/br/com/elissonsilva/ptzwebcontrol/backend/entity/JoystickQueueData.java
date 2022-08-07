package br.com.elissonsilva.ptzwebcontrol.backend.entity;

import lombok.Data;

@Data
public class JoystickQueueData {

    private String startStop;
    private String direction;
    private int speed1;
    private int speed2;

}
