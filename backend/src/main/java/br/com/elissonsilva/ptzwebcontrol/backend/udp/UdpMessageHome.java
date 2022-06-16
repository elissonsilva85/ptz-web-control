package br.com.elissonsilva.ptzwebcontrol.backend.udp;

public class UdpMessageHome extends UdpMessageBase {

    private final String FILTER = "81010604FF";

    public UdpMessageHome() {
        this.setName("Home");
        this.setFilterBase(FILTER);
    }

}
