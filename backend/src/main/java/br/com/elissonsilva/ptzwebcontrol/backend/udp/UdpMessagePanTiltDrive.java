package br.com.elissonsilva.ptzwebcontrol.backend.udp;

import br.com.elissonsilva.ptzwebcontrol.backend.ptz.PtzJoystickDirection;
import br.com.elissonsilva.ptzwebcontrol.backend.ptz.PtzSessionAbstract;
import br.com.elissonsilva.ptzwebcontrol.backend.exception.PtzSessionException;

public class UdpMessagePanTiltDrive extends UdpMessageBase {

    private final String FILTER = "81010601";

    public UdpMessagePanTiltDrive() {
        this.setName("Pan_tiltDrive");
        this.setFilterBase(FILTER);
    }

    @Override
    public void doAction(PtzSessionAbstract session) {
        PtzJoystickDirection direction = null;
        boolean stop = false;
        //
        // UP: 8x 01 06 01 VV WW 03 01 FF
        // VV: Pan speed 0x01 (low speed) to 0x18 (high speed)
        // WW: Tilt speed 0x01 (low speed) to 0x14 (high speed)
        //
        switch(getData().substring(getData().length() - 6)) {
            // --------------------------------
            case "0301FF":
                direction = PtzJoystickDirection.Up;
                break;
            case "0302FF":
                direction = PtzJoystickDirection.Down;
                break;
            case "0103FF":
                direction = PtzJoystickDirection.Left;
                break;
            case "0203FF":
                direction = PtzJoystickDirection.Right;
                break;
            // --------------------------------
            case "0201FF":
                direction = PtzJoystickDirection.RightUp;
                break;
            case "0202FF":
                direction = PtzJoystickDirection.RightDown;
                break;
            case "0101FF":
                direction = PtzJoystickDirection.LeftUp;
                break;
            case "0102FF":
                direction = PtzJoystickDirection.LeftDown;
                break;
            // --------------------------------
            case "0303FF":
                stop = true;
                break;
            // --------------------------------
            default:
                this.logger.warn("Action not found for " + getName());
        }
        //
        int speed1 = 0;
        int speed2 = 0;
        //
        try {
            if (stop) {
                //
                this.logger.info("Running " + getName() + " " + "STOP");
                if(session.isConnected()) session.stopLastCall();
            } else {
                //
                this.logger.info("Running " + getName() + " " + direction.toString());
                if(session.isConnected()) session.startJoystick(direction, speed1, speed2);
            }
        } catch (PtzSessionException e) {
            this.logger.warn("doAction exception : " + e.getMessage(), e);
        }
        //
    }

}
