package br.com.elissonsilva.ptzwebcontrol.backend.udp;

import br.com.elissonsilva.ptzwebcontrol.backend.ptz.PtzSessionAbstract;
import br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.PtzSessionDahua;
import br.com.elissonsilva.ptzwebcontrol.backend.utils.PtzWebControlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UdpMessageCamZoom extends UdpMessageBase {

    protected Logger logger = LoggerFactory.getLogger(UdpMessageCamZoom.class);

    private static int lastSpeed = 7; // standard speed 100%

    public UdpMessageCamZoom() {
        String FILTER = "81010407";

        this.setName("CAM_Zoom");
        this.setFilterBase(FILTER);
    }

    @Override
    public void doAfter(PtzSessionAbstract session) {
        String teleWide = null;
        int speed = lastSpeed;
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
                String teleWideFlag = getData().substring(8,9);
                teleWide = ( "3".equals(teleWideFlag) ? "Wide" : ( "2".equals(teleWideFlag) ? "Tele" : "unknow" ) );
                //
                speed = Integer.valueOf(getData().substring(9,10), 16);
        }
        //
        try {
            //
            if(lastSpeed != speed) {
                lastSpeed = speed;
                int convertedSpeed = PtzWebControlUtils.speedConverter(speed, 0, 7, 1, 100);
                this.logger.info("Setting Zoom Speed to " + convertedSpeed);
                if("PtzSessionDahua".equals(session.getClass().getSimpleName()))
                    ((PtzSessionDahua) session).setZoomSpeed(convertedSpeed);
            }
            //
            switch(teleWide)
            {
                case "STOP":
                    this.logger.info("Running " + getName() + " " + "STOP");
                    if(session.isConnected()) session.stopLastCall();
                    break;
                case "Tele":
                    this.logger.info("Running " + getName() + " " + "STOP");
                    if(session.isConnected()) session.startZoomIn(5);
                    break;
                case "Wide":
                    this.logger.info("Running " + getName() + " " + "STOP");
                    if(session.isConnected()) session.startZoomOut(5);
                    break;
                default:
                    this.logger.info("Action [" + teleWide + "] not defined!");
            }
        } catch (Exception e) {
            this.logger.warn("doAction exception : " + e.getMessage(), e);
        }
        //
    }

}
