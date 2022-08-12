package br.com.elissonsilva.ptzwebcontrol.backend.udp;

import br.com.elissonsilva.ptzwebcontrol.backend.exception.PtzSessionException;
import br.com.elissonsilva.ptzwebcontrol.backend.ptz.PtzSessionAbstract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UdpMessageCamZoomPosInq extends UdpMessageBase {

    protected Logger logger = LoggerFactory.getLogger(UdpMessageCamZoomPosInq.class);

    private int zoom;

    public UdpMessageCamZoomPosInq() {
        String FILTER = "81090447FF";

        this.setName("CAM_ZoomPosInq");
        this.setFilterBase(FILTER);
    }

    @Override
    public void doBefore(PtzSessionAbstract session) {
        //
        try {
            zoom = 0;
            //
            if(session.isConnected()) {
                zoom = session.getZoomValue();
            }
            //
        } catch (PtzSessionException e) {
            this.logger.warn("doAction exception : " + e.getMessage(), e);
        }
        //
    }

    @Override
    public String getResponse() {
        //
        // y0 50 0p 0q 0r 0s FF
        // pqrs: Zoom Position
        //

        int[] z = new int[]{
                zoom/1000, // milhar
                (zoom%1000)/100, // centena
                (zoom%100)/10, // dezena
                (zoom%10) // unidade
        };

        return "9050" +
                "0"+ z[0] + "0" + z[1] + "0" + z[2] + "0" + z[3] +
                "FF";
    }

}
