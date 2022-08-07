package br.com.elissonsilva.ptzwebcontrol.backend.udp;

import br.com.elissonsilva.ptzwebcontrol.backend.ptz.PtzSessionAbstract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UdpMessageZoomDirect extends UdpMessageBase {

    protected Logger logger = LoggerFactory.getLogger(UdpMessageZoomDirect.class);

    public static int zoom;

    public UdpMessageZoomDirect() {
        String FILTER = "81010447";

        this.setName("Zoom Direct");
        this.setFilterBase(FILTER);
    }

    @Override
    public void doAction(PtzSessionAbstract session) {
        //
        // ex: 81 01 04 47 0A 0B 0C 0D 0D 0C 0B 0A FF
        // 8x 01 04 47
        // 0z 0z 0z 0z FF
        //
        // zzzz:
        // 0000 (wide) to 4000 (optical tele) to 5580 (Clear Image Zoom tele 4K)
        // 0000 (wide) to 4000 (optical tele) to 6000 (Clear Image Zoom tele FHD)
        //
        //

        try {
            zoom = Integer.valueOf(getData().substring(9, 10), 16) * 1000 +
                   Integer.valueOf(getData().substring(11, 12), 16) * 100 +
                   Integer.valueOf(getData().substring(13, 14), 16) * 10 +
                   Integer.valueOf(getData().substring(15, 16), 16);

            this.logger.info("Moving Specific Position/Zoom -> " + "Pan: " + UdpMessageAbsolutePosition.pan + " | Tilt: " + UdpMessageAbsolutePosition.tilt + " | Zoom: " + zoom);
            if(session.isConnected()) {
                session.specificPosition(UdpMessageAbsolutePosition.pan, UdpMessageAbsolutePosition.tilt, zoom);
            }

        } catch (Exception e) {
            this.logger.warn("doAction exception : " + e.getMessage(), e);
        }
    }

}
