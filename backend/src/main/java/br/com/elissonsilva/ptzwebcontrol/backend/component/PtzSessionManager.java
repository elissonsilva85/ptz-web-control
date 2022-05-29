package br.com.elissonsilva.ptzwebcontrol.backend.component;

import br.com.elissonsilva.ptzwebcontrol.backend.entity.ConfigPtzConnection;
import br.com.elissonsilva.ptzwebcontrol.backend.exception.PtzSessionManagerBrandNotFoundException;
import br.com.elissonsilva.ptzwebcontrol.backend.exception.PtzSessionManagerException;
import br.com.elissonsilva.ptzwebcontrol.backend.exception.PtzSessionManagerPtzNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

public class PtzSessionManager {

    @Autowired
    private static Config config;

    private static HashMap<String, PtzSessionAbstract> sessionList = new HashMap<String, PtzSessionAbstract>();

    public static void connect(String ptz) throws PtzSessionManagerException {

        String brand = config.getPtz().getBrand();
        ConfigPtzConnection ptzConfig = config.getPtz().getConnection().get(ptz);

        if(ptzConfig == null)
        {
            throw new PtzSessionManagerPtzNotFoundException(ptz);
        }

        switch (brand)
        {
            case "dahua":
                sessionList.put(ptz, new PtzSessionDahua(ptz, ptzConfig.getUser(), ptzConfig.getPassword()));
                break;
            case "confluence":
                //sessionList.put(ptz, new PtzSessionDahua());
                break;
            default:
                throw new PtzSessionManagerBrandNotFoundException(brand);
        }
    }

    public static void disconnect(String ptz) {
        sessionList.remove(ptz);
    }

    public static PtzSessionAbstract getPtz(String ptz) throws PtzSessionManagerException {
        if(!sessionList.containsKey(ptz)) throw new PtzSessionManagerPtzNotFoundException(ptz);

        return sessionList.get(ptz);
    }
}
