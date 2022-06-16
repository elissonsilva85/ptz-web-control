package br.com.elissonsilva.ptzwebcontrol.backend.udp;

import br.com.elissonsilva.ptzwebcontrol.backend.services.UDPServerService;

public class UdpMessagePanTiltPosInq extends UdpMessageBase {

    private final String FILTER = "81090612FF";

    public UdpMessagePanTiltPosInq() {
        this.setName("Pan-tiltPosInq");
        this.setFilterBase(FILTER);
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
