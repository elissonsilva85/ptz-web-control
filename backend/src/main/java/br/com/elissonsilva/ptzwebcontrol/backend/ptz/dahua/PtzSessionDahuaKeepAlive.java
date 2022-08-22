package br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua;

import br.com.elissonsilva.ptzwebcontrol.backend.exception.PtzSessionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PtzSessionDahuaKeepAlive extends Thread {

    private final static Logger logger = LoggerFactory.getLogger(PtzSessionDahuaKeepAlive.class);

    private PtzSessionDahua session;

    public void setSession(PtzSessionDahua session) {
        this.session = session;
    }

    @Override
    public void run() {
        super.run();

        int error = 0;

        while(session.isConnected())
        {
            try {
                session.keepAlive();
                Thread.sleep(9000);
            } catch (InterruptedException e) {
                logger.error("InterruptedException: " + e.getMessage(), e);
                error++;
            } catch (PtzSessionException e) {
                logger.error("PtzSessionException: " + e.getMessage(), e);
                error++;
            }

            if(error > 3) {
                logger.error("Error happened more than 3 times ... stopping thread ...");
                break;
            }
        }

    }
}
