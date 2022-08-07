package br.com.elissonsilva.ptzwebcontrol.backend.udp;

import br.com.elissonsilva.ptzwebcontrol.backend.exception.PtzSessionException;
import br.com.elissonsilva.ptzwebcontrol.backend.ptz.PtzSessionAbstract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UdpMessageHome extends UdpMessageBase {

    protected Logger logger = LoggerFactory.getLogger(UdpMessageHome.class);

    public UdpMessageHome() {
        String FILTER = "81010604FF";

        this.setName("Home");
        this.setFilterBase(FILTER);
    }

    @Override
    public void doAction(PtzSessionAbstract session) {
        //
        try {
            if(!session.isConnected()) {
                this.logger.info("Connecting");
                session.connect();
            }
        } catch (PtzSessionException e) {
            this.logger.warn("doAction exception : " + e.getMessage(), e);
        }
        //
    }

}
