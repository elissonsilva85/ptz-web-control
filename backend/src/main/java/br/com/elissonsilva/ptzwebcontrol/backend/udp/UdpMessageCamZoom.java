package br.com.elissonsilva.ptzwebcontrol.backend.udp;

public class UdpMessageCamZoom extends UdpMessageBase {

    private final String FILTER = "81010407";

    public UdpMessageCamZoom() {
        this.setName("CAM_Zoom");
        this.setFilterBase(FILTER);
    }

    @Override
    public void doAction() {
        String command = "";
        //
        // UP: 8x 01 04 07 VW 01 FF
        // V: Wide / Tele
        // W: Speed - 0 to 7
        //
        switch(getData().substring(getData().length() - 4)) {
            // --------------------------------
            case "02FF":
                command = "Tele (Standard)";
                break;
            case "03FF":
                command = "Wide (Standard)";
                break;
            // --------------------------------
            case "00FF":
                command = "STOP";
                break;
            // --------------------------------
            default:
                // by speed
                String speed = "";
                String teleWide = "";
                /*
                let variable = msg.subarray(4,5).readUInt8();
                let teleWide = ( (variable & 0x30) == 0x30 ? "Wide" : ( (variable & 0x20) == 0x20 ? "Tele" : "unknow"));
                let speed = 0;
                speed += (variable & 1) == 1 ? 1 : 0;
                speed += (variable & 2) == 2 ? 2 : 0;
                speed += (variable & 4) == 4 ? 4 : 0;
                */
                command += "(Speed " + speed + ") " + teleWide;
        }
        //

        //
        this.logger.info("Running " + getName() + " " + command);
        //
    }

}
