package br.com.elissonsilva.ptzwebcontrol.backend.udp;

import br.com.elissonsilva.ptzwebcontrol.backend.ptz.PtzJoystickDirection;
import br.com.elissonsilva.ptzwebcontrol.backend.ptz.PtzSessionAbstract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UdpMessagePanTiltDrive extends UdpMessageBase {

    protected Logger logger = LoggerFactory.getLogger(UdpMessagePanTiltDrive.class);

    public UdpMessagePanTiltDrive() {
        String FILTER = "81010601";

        this.setName("Pan_tiltDrive");
        this.setFilterBase(FILTER);
    }

    @Override
    public void doAction(PtzSessionAbstract session) {
        PtzJoystickDirection direction = null;
        boolean stop = false;
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
        // UP: 8x 01 06 01 VV WW 03 01 FF
        // VV: Pan speed 0x01 (low speed) to 0x18 (high speed)
        // WW: Tilt speed 0x01 (low speed) to 0x14 (high speed)
        //
        int panSpeed = Integer.valueOf(getData().substring(8, 10), 16);
        int tiltSpeed = Integer.valueOf(getData().substring(10, 12), 16);

        //
        try {
            if (stop) {
                //
                this.logger.info("Stop movement");
                if(session.isConnected()) session.stopLastCall();
            } else if(direction != null) {
                //
                this.logger.info("Moving " + direction);
                if(session.isConnected()) {
                    panSpeed = UdpMessageUtils.speedConverter(panSpeed, Integer.valueOf("01", 16), Integer.valueOf("18", 16), 1, 8);
                    tiltSpeed = UdpMessageUtils.speedConverter(tiltSpeed, Integer.valueOf("01", 16), Integer.valueOf("14", 16), 1, 8);
                    //
                    session.startJoystick(direction, panSpeed, tiltSpeed);
                }
            } else {
                this.logger.info("Direction is null!");
            }
        } catch (Exception e) {
            this.logger.warn("doAction exception : " + e.getMessage(), e);
        }
        //
    }

}
