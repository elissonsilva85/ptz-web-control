package br.com.elissonsilva.ptzwebcontrol.backend.services;

import br.com.elissonsilva.ptzwebcontrol.backend.component.Config;
import br.com.elissonsilva.ptzwebcontrol.backend.ptz.PtzSessionAbstract;
import br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.PtzSessionDahua;
import br.com.elissonsilva.ptzwebcontrol.backend.entity.ConfigPtzConnection;
import br.com.elissonsilva.ptzwebcontrol.backend.exception.PtzSessionManagerBrandNotFoundException;
import br.com.elissonsilva.ptzwebcontrol.backend.exception.PtzSessionManagerException;
import br.com.elissonsilva.ptzwebcontrol.backend.exception.PtzSessionManagerPtzNotFoundException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@Scope("singleton")
public class PtzSessionManagerService {

    private final Config config;

    private final static HashMap<String, PtzSessionAbstract> sessionList = new HashMap<>();

    public PtzSessionManagerService(Config config) {
        this.config = config;
    }

    public PtzSessionAbstract getSession(String ptz) throws PtzSessionManagerException {

        if(!sessionList.containsKey(ptz)) {
            ConfigPtzConnection ptzConfig = config.getPtz().getConnection().get(ptz);

            if (ptzConfig == null) {
                throw new PtzSessionManagerPtzNotFoundException(ptz);
            }

            String brand = ptzConfig.getBrand();
            switch (brand) {
                case "dahua":
                    sessionList.put(ptz, new PtzSessionDahua(ptz, ptzConfig.getUser(), ptzConfig.getPassword(), ptzConfig.getUrl()));
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

    public void disconnect(String ptz) {
        sessionList.remove(ptz);
    }

    public PtzSessionAbstract getPtz(String ptz) throws PtzSessionManagerException {
        if(!sessionList.containsKey(ptz)) throw new PtzSessionManagerPtzNotFoundException(ptz);

        return sessionList.get(ptz);
    }
}
