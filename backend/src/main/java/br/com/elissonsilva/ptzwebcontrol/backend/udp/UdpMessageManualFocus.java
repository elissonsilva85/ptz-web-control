package br.com.elissonsilva.ptzwebcontrol.backend.udp;

import br.com.elissonsilva.ptzwebcontrol.backend.ptz.PtzSessionAbstract;
import br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.PtzSessionDahua;
import br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity.config.DahuaParamConfigVideoInFocus.Mode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UdpMessageManualFocus extends UdpMessageBase {

    protected Logger logger = LoggerFactory.getLogger(UdpMessageManualFocus.class);

    public UdpMessageManualFocus() {
        String FILTER = "8101043803FF";

        this.setName("Manual Focus");
        this.setFilterBase(FILTER);
    }

    @Override
    public void doAfter(PtzSessionAbstract session) {

        try {
            this.logger.info("Setting Manual Focus");
            if(session.isConnected()) {
                if("PtzSessionDahua".equals(session.getClass().getSimpleName()))
                    ((PtzSessionDahua) session).setVideoInFocus(Mode.Manual.getValue(), false);
            }

        } catch (Exception e) {
            this.logger.warn("doAction exception : " + e.getMessage(), e);
        }
    }

}
