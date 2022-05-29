package br.com.elissonsilva.ptzwebcontrol.backend.exception;

public abstract class PtzSessionException extends Exception {

    public PtzSessionException() {
        super();
    }

    public PtzSessionException(String message) {
        super(message);
    }
}
