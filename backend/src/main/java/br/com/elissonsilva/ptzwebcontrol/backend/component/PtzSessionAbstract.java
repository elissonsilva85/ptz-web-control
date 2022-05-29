package br.com.elissonsilva.ptzwebcontrol.backend.component;

import br.com.elissonsilva.ptzwebcontrol.backend.entity.JoystickQueueData;
import br.com.elissonsilva.ptzwebcontrol.backend.exception.PtzSessionException;
import br.com.elissonsilva.ptzwebcontrol.backend.exception.PtzSessionManagerException;

import java.util.ArrayList;
import java.util.List;

public abstract class PtzSessionAbstract {

    protected String _lastCallBody = "";
    protected String _httpOptions = "";
    protected String _http; //: HttpClient,

    protected String _ptz;
    protected String _urlBase;
    protected String _user;
    protected String _pass;

    protected List<JoystickQueueData> joystickQueueDataList = new ArrayList<JoystickQueueData>();

    public PtzSessionAbstract(String ptz, String user, String pass) {
        this._user = user;
        this._pass = pass;
        this._ptz = ptz;
        this._urlBase = "http://localhost/ptz/";
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

////////// INTERNAL METHODS /////////////////

protected _getPromiseRejectWithText(text: string): Promise<any> {
        return new Promise<any>( (resolve, reject) => {
        reject(text);
        });
        }

protected _post( page: string, body : any ): Promise<any> {
        return this._http
        .post<any>(this._getUrl(page), body, this._httpOptions)
        .toPromise();
        }

protected _get( page: string, params : string ): Promise<any> {
        return this._http
        .get<any>(this._getUrl(page) + "?" + params, this._httpOptions)
        .toPromise();
        }

protected _getUrl(page: string): string {
        return this._urlBase + this._ptz + "/" + page;
        }
*/

    protected void _getPromiseRejectWithText(String text) {
    }

    protected void _post( String page, String body ) {
    }

    protected void _get( String page, String params) {
    }

    protected String _getUrl(String page) {
        return "";
    }
    ////////// PUBLIC METHODS /////////////////

    public abstract boolean isConnected() throws PtzSessionException;

    public abstract void connect() throws PtzSessionException;

    public abstract void loadPreset(int id) throws PtzSessionException;

    public abstract void savePreset(int id) throws PtzSessionException;

    public abstract void startZoomIn() throws PtzSessionException;

    public abstract void stopZoomIn() throws PtzSessionException;

    public abstract void startZoomOut() throws PtzSessionException;

    public abstract void stopZoomOut() throws PtzSessionException;

    public abstract void startFocusIn() throws PtzSessionException;

    public abstract void stopFocusIn() throws PtzSessionException;

    public abstract void startFocusOut() throws PtzSessionException;

    public abstract void stopFocusOut() throws PtzSessionException;

    public abstract void startJoystick(String direction, int speed1, int speed2) throws PtzSessionException;

    public abstract void stopJoystick(String direction, int speed1, int speed2) throws PtzSessionException;

    public abstract void stopLastCall() throws PtzSessionException;

    /////// UNDER DEVELOPMENT ////////////

    public abstract void specificPosition(int horizontal, int vertical, int zoom) throws PtzSessionException;

    public abstract void getConfig() throws PtzSessionException; // list: any[]

    public abstract void setConfig() throws PtzSessionException; // list: any[], table: any[]

    public abstract void moveDirectly(int[] coord, int speed) throws PtzSessionException;

}
