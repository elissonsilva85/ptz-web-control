package br.com.elissonsilva.ptzwebcontrol.backend.services;

import br.com.elissonsilva.ptzwebcontrol.backend.component.Config;
import br.com.elissonsilva.ptzwebcontrol.backend.component.PtzSessionAbstract;
import br.com.elissonsilva.ptzwebcontrol.backend.dahua.PtzSessionDahua;
import br.com.elissonsilva.ptzwebcontrol.backend.entity.ConfigPtzConnection;
import br.com.elissonsilva.ptzwebcontrol.backend.exception.PtzSessionManagerBrandNotFoundException;
import br.com.elissonsilva.ptzwebcontrol.backend.exception.PtzSessionManagerException;
import br.com.elissonsilva.ptzwebcontrol.backend.exception.PtzSessionManagerPtzNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class PtzSessionManagerService {

    private Config config;

    private HashMap<String, PtzSessionAbstract> sessionList = new HashMap<String, PtzSessionAbstract>();

    public PtzSessionManagerService(Config config) {
        this.config = config;
    }

    public PtzSessionAbstract getSession(String ptz) throws PtzSessionManagerException {

        if(!sessionList.containsKey(ptz)) {
            String brand = config.getPtz().getBrand();
            ConfigPtzConnection ptzConfig = config.getPtz().getConnection().get(ptz);

            if (ptzConfig == null) {
                throw new PtzSessionManagerPtzNotFoundException(ptz);
            }

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