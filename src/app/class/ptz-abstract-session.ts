import { HttpClient, HttpHeaders } from "@angular/common/http";

export abstract class PtzAbstractSession {

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

  public abstract isConnected() : boolean;

  public abstract connect() : Promise<any>;

  public abstract loadPreset(id: number) : Promise<any>;

  public abstract savePreset(id: number) : Promise<any>;

  public abstract startZoomIn() : Promise<any>;

  public abstract stopZoomIn() : Promise<any>;

  public abstract startZoomOut() : Promise<any>;

  public abstract stopZoomOut() : Promise<any>

  public abstract startFocusIn() : Promise<any>;

  public abstract stopFocusIn() : Promise<any>;

  public abstract startFocusOut() : Promise<any>;

  public abstract stopFocusOut() : Promise<any>;

  public abstract startJoystick(direction: string, speed1: number, speed2: number) : Promise<any>;

  public abstract stopJoystick(direction: string, speed1: number, speed2: number) : Promise<any>

  public abstract stopLastCall() : Promise<any>;

  /////// UNDER DEVELOPMENT ////////////

  public abstract specificPosition(horizontal: number, vertical: number, zoom: number) : Promise<any>;

  public abstract getConfig(list: any[]) : Promise<any>;

  public abstract setConfig(list: any[], table: any[]) : Promise<any>;

  public abstract moveDirectly(coord: number[], speed: number) : Promise<any>;

}
