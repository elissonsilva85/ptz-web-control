import { HttpClient, HttpHeaders } from "@angular/common/http";

export abstract class PtzAbstractSession {

  protected _httpOptions = {
      headers: new HttpHeaders({
        'X-Requested-With': 'XMLHttpRequest',
        'Accept': 'application/json, text/javascript, */*; q=0.01',
        'Accept-Language': 'pt-PT,pt;q=0.9,en-US;q=0.8,en;q=0.7',
        'Content-Type': 'application/json',
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

  public connect() : Promise<any> {

    return this._post("connect", "").then( r => {
      this._addLog(this._ptz, "login success: " + JSON.stringify(r));
    }).catch( e => {
      this._addLog(this._ptz, "login error: " + JSON.stringify(e));
    });

  }

  public loadPreset(id: number) : Promise<any> {
    return this._get("preset", "id=" + id)
    .then( r => {
      this._addLog(this._ptz, "loadPreset return: " + JSON.stringify(r));
    });
  }

  public savePreset(id: number, name: string) : Promise<any> {
    return this._post("preset", { id: id, name: name })
      .then( r => {
        this._addLog(this._ptz, "savePreset return: " + JSON.stringify(r));
      });
  }

  public getPresetNames() : Promise<any> {
    return this._get("presetNames", "")
    .then( r => {
      this._addLog(this._ptz, "getPresetNames return: " + JSON.stringify(r));
      return r;
    });
  }

  public getCurrentPosition() : Promise<any> {
    return this._get("currentPostion", "")
    .then( r => {
      this._addLog(this._ptz, "currentPostion return: " + JSON.stringify(r));
      return r;
    });
  }

  public getZoomValue() : Promise<any> {
    return this._get("zoomValue", "")
    .then( r => {
      this._addLog(this._ptz, "getZoomValue return: " + JSON.stringify(r));
      return r;
    });
  }

  public startZoomIn() : Promise<any> {
    let amount = 5;
    return this._post("zoomInStart", { amount: amount })
  }

  public stopZoomIn() : Promise<any> {
    let amount = 5;
    return this._post("zoomInStop", { amount: amount })
  }

  public startZoomOut() : Promise<any> {
    let amount = 5;
    return this._post("zoomOutStart", { amount: amount })
  }

  public stopZoomOut() : Promise<any> {
    let amount = 5;
    return this._post("zoomOutStop", { amount: amount })
  }

  public startFocusIn() : Promise<any> {
    let amount = 5;
    return this._post("focusInStart", { amount: amount })
  }

  public stopFocusIn() : Promise<any> {
    let amount = 5;
    return this._post("focusInStop", { amount: amount })
  }

  public startFocusOut() : Promise<any> {
    let amount = 5;
    return this._post("focusOutStart", { amount: amount })
  }

  public stopFocusOut() : Promise<any> {
    let amount = 5;
    return this._post("focusOutStop", { amount: amount })
  }

  public startIrisLarge() : Promise<any> {
    let amount = 5;
    return this._post("irisLargeStart", { amount: amount })
  }

  public stopIrisLarge() : Promise<any> {
    let amount = 5;
    return this._post("irisLargeStop", { amount: amount })
  }

  public startIrisSmall() : Promise<any> {
    let amount = 5;
    return this._post("irisSmallStart", { amount: amount })
  }

  public stopIrisSmall() : Promise<any> {
    let amount = 5;
    return this._post("irisSmallStop", { amount: amount })
  }

  public startJoystick(direction: string, speed1: number, speed2: number) : Promise<any> {
    let body = {
      "direction": direction,
      "speed1": speed1,
      "speed2": speed2
    };

    return this._post("joystickStart", body);
  }

  public stopJoystick(direction: string, speed1: number, speed2: number) : Promise<any> {
    let body = {
      "direction": direction,
      "speed1": speed1,
      "speed2": speed2
    };

    return this._post("joystickStop", body);
  }

  public stopLastCall() : Promise<any> {
    return this._post("stopLastCall", "");
  }

  public specificPosition(horizontal: number, vertical: number, zoom: number) : Promise<any> {
    let body = {
      "horizontal": horizontal,
      "vertical": vertical,
      "zoom": zoom
    };

    return this._post("specificPosition", body);
  };

  public setZoomSpeed(amount: number) : Promise<any> {
    return this._post("setZoomSpeed", { amount: amount })
    .then( r => {
      this._addLog(this._ptz, "setZoomSpeed return: " + JSON.stringify(r));
    });
  }

}
