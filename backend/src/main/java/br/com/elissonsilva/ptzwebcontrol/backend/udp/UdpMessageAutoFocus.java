package br.com.elissonsilva.ptzwebcontrol.backend.udp;

public class UdpMessageAutoFocus extends UdpMessageBase {

    private final String FILTER = "8101043802FF";

    public UdpMessageAutoFocus() {
        this.setName("Auto Focus");
        this.setFilterBase(FILTER);
    }

}
