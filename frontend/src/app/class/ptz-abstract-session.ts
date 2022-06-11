import { HttpClient, HttpHeaders } from "@angular/common/http";

export abstract class PtzAbstractSession {

  protected _isConnected : boolean = false;
  protected _lastCallBody : any = {};
  protected _httpOptions = {
      headers: new HttpHeaders({
        'X-Requested-With': 'XMLHttpRequest',
        'Accept': 'application/json, text/javascript, */*; q=0.01',
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

  ////////// PUBLIC METHODS /////////////////

  public isConnected() : boolean {
    return this._isConnected;
  }

  public connect() : Promise<any> {

    return this._post("connect", "").then( r => {
      this._addLog(this._ptz, "login return: " + JSON.stringify(r));
      this._isConnected = true;
    }).catch( e => {
      this._addLog(this._ptz, "login return: " + JSON.stringify(e));
      this._isConnected = false;
    });

  }

  public loadPreset(id: number) : Promise<any> {
    if( !this.isConnected() )
      return this._getPromiseRejectWithText(`loadPreset: ${this._ptz} is not connected`);

    return this._get("preset/" + id, "")
    .then( r => {
      this._addLog(this._ptz, "loadPreset return: " + JSON.stringify(r));
    });
  }

  public savePreset(id: number) : Promise<any> {
    return this._post("preset/" + id, "")
      .then( r => {
        this._addLog(this._ptz, "savePreset return: " + JSON.stringify(r));
      });
  }

  public startZoomIn() : Promise<any> {
    let amount = 5;
    return this._post("zoomIn/start/" + amount, "");
  }

  public stopZoomIn() : Promise<any> {
    let amount = 5;
    return this._post("zoomIn/stop/" + amount, "");
  }

  public startZoomOut() : Promise<any> {
    let amount = 5;
    return this._post("zoomOut/start/" + amount, "");
  }

  public stopZoomOut() : Promise<any> {
    let amount = 5;
    return this._post("zoomOut/stop/" + amount, "");
  }

  public startFocusIn() : Promise<any> {
    let amount = 5;
    return this._post("focusIn/start/" + amount, "");
  }

  public stopFocusIn() : Promise<any> {
    let amount = 5;
    return this._post("focusIn/stop/" + amount, "");
  }

  public startFocusOut() : Promise<any> {
    let amount = 5;
    return this._post("focusOut/start/" + amount, "");
  }

  public stopFocusOut() : Promise<any> {
    let amount = 5;
    return this._post("focusOut/stop/" + amount, "");
  }

  public startJoystick(direction: string, speed1: number, speed2: number) : Promise<any> {

    let body = {
      "direction": direction,
      "speed1": speed1,
      "speed2": speed2
    };

    return this._post("joystick/start", body);
  }

  public stopJoystick(direction: string, speed1: number, speed2: number) : Promise<any> {
    
    let body = {
      "direction": direction,
      "speed1": speed1,
      "speed2": speed2
    };

    return this._post("joystick/stop", body);
  }

  public stopLastCall() : Promise<any> {

    return this._post("stopLastCall", "");

  }

  /////// UNDER DEVELOPMENT ////////////

  public abstract specificPosition(horizontal: number, vertical: number, zoom: number) : Promise<any>;

  public abstract getConfig(list: any[]) : Promise<any>;

  public abstract setConfig(list: any[], table: any[]) : Promise<any>;

  public abstract moveDirectly(coord: number[], speed: number) : Promise<any>;

}
