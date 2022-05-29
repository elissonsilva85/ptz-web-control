package br.com.elissonsilva.ptzwebcontrol.backend.exception;

public class PtzSessionManagerBrandNotFoundException extends PtzSessionManagerException {

    public PtzSessionManagerBrandNotFoundException(String brand) {
        super("Unknown brand name [" + brand + "]");
    }

}
