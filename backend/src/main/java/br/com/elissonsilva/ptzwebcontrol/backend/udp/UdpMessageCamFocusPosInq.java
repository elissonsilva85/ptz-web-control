package br.com.elissonsilva.ptzwebcontrol.backend.udp;

public class UdpMessageCamFocusPosInq extends UdpMessageBase {

    private final String FILTER = "81090448FF";

    public UdpMessageCamFocusPosInq() {
        this.setName("CAM_FocusPosInq");
        this.setFilterBase("81090448FF");
    }

    @Override
    public String getResponse() {
        //
        // y0 50 0p 0q 0r 0s FF
        // pqrs: Focus Position
        //
        String response = "9050" + "00000000" + "FF";
        return response;
    }

}
