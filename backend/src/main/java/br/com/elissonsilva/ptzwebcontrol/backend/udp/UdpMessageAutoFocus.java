package br.com.elissonsilva.ptzwebcontrol.backend.udp;

import br.com.elissonsilva.ptzwebcontrol.backend.ptz.PtzSessionAbstract;
import br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.PtzSessionDahua;
import br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity.config.DahuaParamConfigVideoInFocus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UdpMessageAutoFocus extends UdpMessageBase {

    protected Logger logger = LoggerFactory.getLogger(UdpMessageAutoFocus.class);

    public UdpMessageAutoFocus() {
        String FILTER = "8101043802FF";

        this.setName("Auto Focus");
        this.setFilterBase(FILTER);
    }

    @Override
    public void doAfter(PtzSessionAbstract session) {

        try {
            this.logger.info("Setting Automatic Focus");
            if(session.isConnected()) {
                if("PtzSessionDahua".equals(session.getClass().getSimpleName()))
                    ((PtzSessionDahua) session).setVideoInFocus(DahuaParamConfigVideoInFocus.Mode.Automatico.getValue(), false);
            }

        } catch (Exception e) {
            this.logger.warn("doAction exception : " + e.getMessage(), e);
        }
    }

}
