package br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.exception;

import br.com.elissonsilva.ptzwebcontrol.backend.exception.PtzSessionException;

import java.io.IOException;

public class PtzSessionDahuaException extends PtzSessionException {

    public PtzSessionDahuaException() {
        super();
    }

    public PtzSessionDahuaException(String message) {
        super(message);
    }

    public PtzSessionDahuaException(Exception e) { super(e); }
}
