package br.com.elissonsilva.ptzwebcontrol.backend.udp;

import br.com.elissonsilva.ptzwebcontrol.backend.exception.PtzSessionException;
import br.com.elissonsilva.ptzwebcontrol.backend.ptz.PtzSessionAbstract;

public class UdpMessagePanTiltPosInq extends UdpMessageBase {

    private final String FILTER = "81090612FF";

    public UdpMessagePanTiltPosInq() {
        this.setName("Pan-tiltPosInq");
        this.setFilterBase(FILTER);
    }

    @Override
    public void doAction(PtzSessionAbstract session) {
        //
        try {
            this.logger.info("Running " + getName() + " " + "Connection");
            if(!session.isConnected()) session.connect();
        } catch (PtzSessionException e) {
            this.logger.warn("doAction exception : " + e.getMessage(), e);
        }
        //
    }

    @Override
    public String getResponse() {
        //
        // y0 50 0w 0w 0w 0w 0z 0z 0z 0z FF
        // wwww: Pan Position
        // zzzz: Tilt Position
        //
        String response = "9050" + "00000000" + "00000000" + "FF";
        return response;
    }
}
