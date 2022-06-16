package br.com.elissonsilva.ptzwebcontrol.backend.udp;

import br.com.elissonsilva.ptzwebcontrol.backend.exception.PtzSessionException;
import br.com.elissonsilva.ptzwebcontrol.backend.ptz.PtzSessionAbstract;

public class UdpMessageCamZoom extends UdpMessageBase {

    private final String FILTER = "81010407";

    public UdpMessageCamZoom() {
        this.setName("CAM_Zoom");
        this.setFilterBase(FILTER);
    }

    @Override
    public void doAction(PtzSessionAbstract session) {
        String teleWide = "";
        int speed = 5; // standard speed
        //
        // UP: 8x 01 04 07 VW 01 FF
        // V: Wide / Tele
        // W: Speed - 0 to 7
        //
        switch(getData().substring(getData().length() - 4)) {
            // --------------------------------
            case "02FF":
                teleWide = "Tele";
                break;
            case "03FF":
                teleWide = "Wide";
                break;
            // --------------------------------
            case "00FF":
                teleWide = "STOP";
                break;
            // --------------------------------
            default:
                // by speed
                /*
                String variable = msg.subarray(4,5).readUInt8();
                teleWide = ( (variable & 0x30) == 0x30 ? "Wide" : ( (variable & 0x20) == 0x20 ? "Tele" : "unknow"));
                speed = 0;
                speed += (variable & 1) == 1 ? 1 : 0;
                speed += (variable & 2) == 2 ? 2 : 0;
                speed += (variable & 4) == 4 ? 4 : 0;
                */
        }
        //
        try {
            if ("STOP".equals(teleWide)) {
                //
                this.logger.info("Running " + getName() + " " + "STOP");
                if(session.isConnected()) session.stopLastCall();
            } else if ("Tele".equals(teleWide)) {
                //
                this.logger.info("Running " + getName() + " " + teleWide);
                if(session.isConnected()) session.startZoomIn(speed);
            } else if ("Wide".equals(teleWide)) {
                //
                this.logger.info("Running " + getName() + " " + teleWide);
                if(session.isConnected()) session.startZoomOut(speed);
            }
        } catch (PtzSessionException e) {
            this.logger.warn("doAction exception : " + e.getMessage(), e);
        }
        //
    }

}
