package br.com.elissonsilva.ptzwebcontrol.backend.udp;

import br.com.elissonsilva.ptzwebcontrol.backend.exception.PtzSessionException;
import br.com.elissonsilva.ptzwebcontrol.backend.ptz.PtzSessionAbstract;

public class UdpMessageCamFocus extends UdpMessageBase {

    private final String FILTER = "81010408";

    public UdpMessageCamFocus() {
        this.setName("CAM_Focus");
        this.setFilterBase(FILTER);
    }

    @Override
    public void doAction(PtzSessionAbstract session) {
        String farNear = "";
        int speed = 5; // standard speed
        //
        // UP: 8x 01 04 07 VW 01 FF
        // V: Far / Near
        // W: Speed - 0 to 7
        //
        switch(getData().substring(getData().length() - 4)) {
            // --------------------------------
            case "02FF":
                farNear = "Far";
                break;
            case "03FF":
                farNear = "Near";
                break;
            // --------------------------------
            case "00FF":
                farNear = "STOP";
                break;
            // --------------------------------
            default:
                /*
                String variable = msg.subarray(4,5).readUInt8();
                farNear = ( (variable & 0x30) == 0x30 ? "Near" : ( (variable & 0x20) == 0x20 ? "Far" : "unknow"));
                speed = 0;
                speed += (variable & 1) == 1 ? 1 : 0;
                speed += (variable & 2) == 2 ? 2 : 0;
                speed += (variable & 4) == 4 ? 4 : 0;
                */
        }
        //
        try {
            if ("STOP".equals(farNear)) {
                //
                this.logger.info("Running " + getName() + " " + "STOP");
                if(session.isConnected()) session.stopLastCall();
            } else if ("Near".equals(farNear)) {
                //
                this.logger.info("Running " + getName() + " " + farNear);
                if(session.isConnected()) session.startFocusIn(speed);
            } else if ("Far".equals(farNear)) {
                //
                this.logger.info("Running " + getName() + " " + farNear);
                if(session.isConnected()) session.startFocusOut(speed);
            }
        } catch (PtzSessionException e) {
            this.logger.warn("doAction exception : " + e.getMessage(), e);
        }
        //
    }
}
