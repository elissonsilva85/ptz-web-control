package br.com.elissonsilva.ptzwebcontrol.backend.udp;

public class UdpMessagePanTiltDrive extends UdpMessageBase {

    private final String FILTER = "81010601";

    public UdpMessagePanTiltDrive() {
        this.setName("Pan_tiltDrive");
        this.setFilterBase(FILTER);
    }

    @Override
    public void doAction() {
        String direction = "";
        //
        // UP: 8x 01 06 01 VV WW 03 01 FF
        // VV: Pan speed 0x01 (low speed) to 0x18 (high speed)
        // WW: Tilt speed 0x01 (low speed) to 0x14 (high speed)
        //
        switch(getData().substring(getData().length() - 6)) {
            // --------------------------------
            case "0301FF":
                direction = "UP";
                break;
            case "0302FF":
                direction = "DOWN";
                break;
            case "0103FF":
                direction = "LEFT";
                break;
            case "0203FF":
                direction = "RIGHT";
                break;
            // --------------------------------
            case "0201FF":
                direction = "UP RIGHT";
                break;
            case "0202FF":
                direction = "DOWN RIGHT";
                break;
            case "0101FF":
                direction = "UP LEFT";
                break;
            case "0102FF":
                direction = "DOWN LEFT";
                break;
            // --------------------------------
            case "0303FF":
                direction = "STOP";
                break;
            // --------------------------------
            default:
                this.logger.warn("Action not found for " + getName());
        }
        //
        this.logger.info("Running " + getName() + " " + direction);
        //
    }

}
