package br.com.elissonsilva.ptzwebcontrol.backend.udp;

import br.com.elissonsilva.ptzwebcontrol.backend.exception.PtzSessionException;
import br.com.elissonsilva.ptzwebcontrol.backend.ptz.PtzSessionAbstract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UdpMessageCamFocus extends UdpMessageBase {

    protected Logger logger = LoggerFactory.getLogger(UdpMessageCamFocus.class);

    public UdpMessageCamFocus() {
        String FILTER = "81010408";

        this.setName("CAM_Focus");
        this.setFilterBase(FILTER);
    }

    @Override
    public void doAction(PtzSessionAbstract session) {
        String farNear = null;
        //int speed = 5; // standard speed
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
                // by speed
                String farNearFlag = getData().substring(8,9);
                farNear = ( "3".equals(farNearFlag) ? "Near" : ( "2".equals(farNearFlag) ? "Far" : "unknow" ) );
                //
                //speed = Integer.valueOf(getData().substring(9,10), 16);
        }
        //
        try {
            switch(farNear)
            {
                case "STOP":
                    this.logger.info("Running " + getName() + " " + "STOP");
                    if(session.isConnected()) session.stopLastCall();
                    break;
                case "Near":
                    this.logger.info("Running " + getName() + " " + farNear);
                    if(session.isConnected()) session.startFocusIn(5);
                    break;
                case "Far":
                    this.logger.info("Running " + getName() + " " + farNear);
                    if(session.isConnected()) session.startFocusOut(5);
                    break;
                default:
                    this.logger.info("Action [" + farNear + "] not defined!");
            }
        } catch (PtzSessionException e) {
            this.logger.warn("doAction exception : " + e.getMessage(), e);
        }
        //
    }
}
