package br.com.elissonsilva.ptzwebcontrol.backend.entity;

import lombok.Data;

@Data
public class DahuaSessionData {

    private String random;
    private String realm;
    private String session;
    private int result;
    private int id;
    private String timer;
    private boolean isConnected;

}
