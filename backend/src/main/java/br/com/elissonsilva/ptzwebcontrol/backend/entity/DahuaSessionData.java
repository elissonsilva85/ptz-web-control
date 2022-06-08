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

    public DahuaSessionData(String random, String realm, String session, boolean isConnected) {
        this.random = random;
        this.realm = realm;
        this.session = session;
        this.isConnected = isConnected;
    }

    public void updateSession(int id) {
        this.id = id;
    }

    public void updateSession(String session, int id) {
        this.session = session;
        this.id = id;

    }

    public void updateSession(int id, int result) {
        this.id = id;
        this.result = result;
    }

    public int getNextId() {
        return this.id + 1;
    }
}
