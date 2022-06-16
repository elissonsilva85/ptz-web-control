package br.com.elissonsilva.ptzwebcontrol.backend.entity;

import br.com.elissonsilva.ptzwebcontrol.backend.ptz.PtzJoystickDirection;
import lombok.Data;

@Data
public class JoystickRequest {

    private PtzJoystickDirection direction;
    private int speed1;
    private int speed2;

}
