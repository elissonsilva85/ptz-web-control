package br.com.elissonsilva.ptzwebcontrol.backend.udp;

import br.com.elissonsilva.ptzwebcontrol.backend.exception.PtzSessionException;
import br.com.elissonsilva.ptzwebcontrol.backend.ptz.PtzSessionAbstract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UdpMessageCamFocusPosInq extends UdpMessageBase {

    protected Logger logger = LoggerFactory.getLogger(UdpMessageCamFocusPosInq.class);

    private int focus;

    public UdpMessageCamFocusPosInq() {
        String FILTER = "81090448FF";

        this.setName("CAM_FocusPosInq");
        this.setFilterBase(FILTER);
    }

    @Override
    public void doBefore(PtzSessionAbstract session) {
        //
        try {
            focus = 0;
            //
            if(session.isConnected()) {
                //
            }
        } catch (PtzSessionException e) {
            this.logger.warn("doAction exception : " + e.getMessage(), e);
        }
        //
    }

    @Override
    public String getResponse() {
        //
        // y0 50 0p 0q 0r 0s FF
        // pqrs: Focus Position
        //

        int[] f = new int[]{
                focus/1000, // milhar
                (focus%1000)/100, // centena
                (focus%100)/10, // dezena
                (focus%10) // unidade
        };

        return "9050" +
                "0"+ f[0] + "0" + f[1] + "0" + f[2] + "0" + f[3] +
                "FF";
    }

}
