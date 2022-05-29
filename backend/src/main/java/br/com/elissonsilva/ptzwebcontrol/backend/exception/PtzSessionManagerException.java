package br.com.elissonsilva.ptzwebcontrol.backend.exception;

public abstract class PtzSessionManagerException extends Exception {

    public PtzSessionManagerException() {
        super();
    }

    public PtzSessionManagerException(String message) {
        super(message);
    }
}
