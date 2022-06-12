package br.com.elissonsilva.ptzwebcontrol.backend.entity;

import lombok.Data;

@Data
public class JoystickRequest {

    private String direction;
    private int speed1;
    private int speed2;

}
