package br.com.elissonsilva.ptzwebcontrol.backend.udp;

public class UdpMessageCamZoomPosInq extends UdpMessageBase {

    private final String FILTER = "81090447FF";

    public UdpMessageCamZoomPosInq() {
        this.setName("CAM_ZoomPosInq");
        this.setFilterBase(FILTER);
    }

    @Override
    public String getResponse() {
        //
        // y0 50 0p 0q 0r 0s FF
        // pqrs: Zoom Position
        //
        String response = "9050" + "00000000" + "FF";
        return response;
    }

}
