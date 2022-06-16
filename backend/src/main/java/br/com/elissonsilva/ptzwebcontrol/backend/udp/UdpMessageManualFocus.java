package br.com.elissonsilva.ptzwebcontrol.backend.udp;

public class UdpMessageManualFocus extends UdpMessageBase {

    private final String FILTER = "8101043803FF";

    public UdpMessageManualFocus() {
        this.setName("Manual Focus");
        this.setFilterBase(FILTER);
    }

}
