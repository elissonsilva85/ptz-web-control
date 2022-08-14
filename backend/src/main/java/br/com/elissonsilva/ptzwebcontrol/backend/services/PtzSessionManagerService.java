package br.com.elissonsilva.ptzwebcontrol.backend.services;

import br.com.elissonsilva.ptzwebcontrol.backend.component.Configuration;
import br.com.elissonsilva.ptzwebcontrol.backend.entity.ConfigPtzConnection;
import br.com.elissonsilva.ptzwebcontrol.backend.exception.PtzSessionException;
import br.com.elissonsilva.ptzwebcontrol.backend.exception.PtzSessionManagerBrandNotFoundException;
import br.com.elissonsilva.ptzwebcontrol.backend.exception.PtzSessionManagerException;
import br.com.elissonsilva.ptzwebcontrol.backend.exception.PtzSessionManagerPtzNotFoundException;
import br.com.elissonsilva.ptzwebcontrol.backend.ptz.PtzSessionAbstract;
import br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.PtzSessionDahua;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@Scope("singleton")
public class PtzSessionManagerService {

    private final static Logger logger = LoggerFactory.getLogger(PtzSessionManagerService.class);

    private final Configuration configuration;

    private final static HashMap<String, PtzSessionAbstract> sessionList = new HashMap<>();

    public PtzSessionManagerService(Configuration configuration) {
        this.configuration = configuration;
    }

    public PtzSessionAbstract getSession(String ptz) throws PtzSessionManagerException {

        if(!sessionList.containsKey(ptz)) {
            ConfigPtzConnection ptzConfig = configuration.getPtz().getConnection().get(ptz);

            if (ptzConfig == null) {
                throw new PtzSessionManagerPtzNotFoundException(ptz);
            }

            String brand = ptzConfig.getBrand();
            switch (brand) {
                case "dahua":
                    sessionList.put(ptz, new PtzSessionDahua(ptz, ptzConfig.getUser(), ptzConfig.getPassword(), ptzConfig.getUrl()){{
                        try {
                            connect();
                        } catch (PtzSessionException e) {
                            logger.error("Brand " + brand + " PTZ [" + ptz + "] Connection error: " + e.getMessage(), e);
                        }
                    }});
                    break;
                case "confluence":
                    //sessionList.put(ptz, new PtzSessionDahua());
                    break;
                default:
                    throw new PtzSessionManagerBrandNotFoundException(brand);
            }
        }

        return sessionList.get(ptz);
    }

    public PtzSessionAbstract getPtz(String ptz) throws PtzSessionManagerException {
        if(!sessionList.containsKey(ptz)) throw new PtzSessionManagerPtzNotFoundException(ptz);

        return sessionList.get(ptz);
    }
}
