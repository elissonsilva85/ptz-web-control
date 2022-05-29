package br.com.elissonsilva.ptzwebcontrol.backend.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
public class JoystickQueueData {

    private String startStop;
    private String direction;
    private int speed1;
    private int speed2;

}
