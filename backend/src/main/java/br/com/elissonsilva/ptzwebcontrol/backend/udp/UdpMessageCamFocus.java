package br.com.elissonsilva.ptzwebcontrol.backend.udp;

public class UdpMessageCamFocus extends UdpMessageBase {

    private final String FILTER = "81010408";

    public UdpMessageCamFocus() {
        this.setName("CAM_Focus");
        this.setFilterBase(FILTER);
    }

    @Override
    public void doAction() {
        String command = "";
        //
        // UP: 8x 01 04 07 VW 01 FF
        // V: Far / Near
        // W: Speed - 0 to 7
        //
        switch(getData().substring(getData().length() - 4)) {
            // --------------------------------
            case "02FF":
                command = "Far (Standard)";
                break;
            case "03FF":
                command = "Near (Standard)";
                break;
            // --------------------------------
            case "00FF":
                command = "STOP";
                break;
            // --------------------------------
            default:
                // by speed
                String speed = "";
                String farNear = "";
                /*
                let variable = msg.subarray(4,5).readUInt8();
                let farNear = ( (variable & 0x30) == 0x30 ? "Near" : ( (variable & 0x20) == 0x20 ? "Far" : "unknow"));
                let speed = 0;
                speed += (variable & 1) == 1 ? 1 : 0;
                speed += (variable & 2) == 2 ? 2 : 0;
                speed += (variable & 4) == 4 ? 4 : 0;
                */
                command += "(Speed " + speed + ") " + farNear;
        }
        //

        //
        this.logger.info("Running " + getName() + " " + command);
        //
    }
}
