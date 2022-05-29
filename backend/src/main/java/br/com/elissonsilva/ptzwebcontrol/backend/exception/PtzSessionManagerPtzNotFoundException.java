package br.com.elissonsilva.ptzwebcontrol.backend.exception;

public class PtzSessionManagerPtzNotFoundException extends PtzSessionManagerException {

    public PtzSessionManagerPtzNotFoundException(String ptz) {
        super("Not found config for ptz [" + ptz + "]");
    }

}
