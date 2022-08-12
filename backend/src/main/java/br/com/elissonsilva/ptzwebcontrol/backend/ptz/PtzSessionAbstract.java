package br.com.elissonsilva.ptzwebcontrol.backend.ptz;

import br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity.param.DahuaParamRequestSetConfig;
import br.com.elissonsilva.ptzwebcontrol.backend.entity.JoystickQueueData;
import br.com.elissonsilva.ptzwebcontrol.backend.exception.PtzSessionException;
import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class PtzSessionAbstract {

    protected String _lastCallBody = "";
    protected String _httpOptions = "";
    protected String _http; //: HttpClient,

    protected OkHttpClient httpClient;

    protected Headers.Builder headers;

    protected String _ptz;
    protected String _urlBase;
    protected String _user;
    protected String _pass;

    protected List<JoystickQueueData> joystickQueueDataList = new ArrayList<>();

    public PtzSessionAbstract(String ptz, String user, String pass, String url) {
        this._user = user;
        this._pass = pass;
        this._ptz = ptz;
        this._urlBase = url;
        this.httpClient = new OkHttpClient.Builder().build();

        //log.debug("--- HEADERS ---------------------------");
        this.headers = new Headers.Builder();
        headers.add("X-Requested-With", "XMLHttpRequest");
        headers.add("Accept", "application/json, text/javascript, */*; q=0.01");
        headers.add("Accept-Language", "pt-PT,pt;q=0.9,en-US;q=0.8,en;q=0.7");
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        //log.debug("---------------------------------------");
    }

    //protected _addLog = (p:string, t:string) => {}) {

    /*
protected _lastCallBody : any = {};
protected _httpOptions = {
        headers: new HttpHeaders({
        'X-Requested-With': 'XMLHttpRequest',
        'Accept-Language': 'pt-PT,pt;q=0.9,en-US;q=0.8,en;q=0.7',
        'Content-Type': 'application/x-www-form-urlencoded',
        }),
        withCredentials: false,
        params: null
        };

        constructor(
protected _http: HttpClient,
protected _ptz : string,
protected _urlBase : string,
protected _user : string,
protected _pass : string,
protected _addLog = (p:string, t:string) => {}) {
        }

*/

    protected Response _post( String page, String postBody ) throws IOException {
        RequestBody requestBody = RequestBody.create(postBody, MediaType.parse("application/json; charset=utf-8"));

        Request.Builder builder = new Request.Builder()
                .headers(headers.build())
                .url(page)
                .method("POST", requestBody);

        return httpClient.newCall(builder.build()).execute();
    }

    protected Response _get(String page, String params) throws IOException {
        Request.Builder builder = new Request.Builder()
                .headers(headers.build())
                .url(this._getUrl(page) + "?" + params)
                .method("GET", null);

        return httpClient.newCall(builder.build()).execute();
    }

    protected String _getUrl(String page) {
        //  return this._urlBase + this._ptz + "/" + page;
        return this._urlBase + "/" + page;
    }
    ////////// PUBLIC METHODS /////////////////

    public abstract boolean isConnected() throws PtzSessionException;

    public abstract void connect() throws PtzSessionException;

    public abstract void loadPreset(int id) throws PtzSessionException;

    public abstract void savePreset(int id, String name) throws PtzSessionException;

    public abstract int getZoomValue() throws PtzSessionException;

    public abstract int[] getViewAngles() throws PtzSessionException;

    public abstract void startZoomIn(int amount ) throws PtzSessionException;

    public abstract void stopZoomIn(int amount ) throws PtzSessionException;

    public abstract void startZoomOut(int amount ) throws PtzSessionException;

    public abstract void stopZoomOut(int amount ) throws PtzSessionException;

    public abstract void startFocusIn(int amount ) throws PtzSessionException;

    public abstract void stopFocusIn(int amount ) throws PtzSessionException;

    public abstract void startFocusOut(int amount ) throws PtzSessionException;

    public abstract void stopFocusOut(int amount ) throws PtzSessionException;

    public abstract void startJoystick(PtzJoystickDirection direction, int speed1, int speed2) throws PtzSessionException;

    public abstract void stopJoystick(PtzJoystickDirection direction, int speed1, int speed2) throws PtzSessionException;

    public abstract void stopLastCall() throws PtzSessionException;

    /////// UNDER DEVELOPMENT ////////////

    public abstract void specificPosition(int horizontal, int vertical, int zoom) throws PtzSessionException;

    public abstract void getConfig() throws PtzSessionException; // list: any[]

    public abstract void setConfig(List<DahuaParamRequestSetConfig> setConfigList) throws PtzSessionException; // list: any[], table: any[]

    public abstract void moveDirectly(int[] coord) throws PtzSessionException;

}
