import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Faultylabs } from '../class/faultylabs';
import { RcpSessionData } from '../class/rcp-session-data';

const faultylabs: Faultylabs = new Faultylabs();

export class RcpSession {

    private _keepAliveInterval: number = 9000;
    private _sessionData : RcpSessionData = new RcpSessionData();
    private _lastCallBody : any = {};
    private _httpOptions = {
        headers: new HttpHeaders({
          'X-Requested-With': 'XMLHttpRequest',
          'Accept': 'application/json, text/javascript, */*; q=0.01',
          'Accept-Language': 'pt-PT,pt;q=0.9,en-US;q=0.8,en;q=0.7',
          'Content-Type': 'application/x-www-form-urlencoded',
        }),
        withCredentials: false,
        params: null
      };

    //private _runningKeepAliveEvent: Observable<boolean>;

    constructor(
      private _http: HttpClient,
      private _ptz : string,
      private _urlBase : string,
      private _user : string,
      private _pass : string,
      private _addLog = (p:string, t:string) => {}) {
    }

    private _getPromiseRejectWithText(text: string): Promise<any> {
      return new Promise<any>( (resolve, reject) => {
        reject(text);
      });
    }

    public isConnected() : boolean {
      return this._sessionData.isConnected;
    }

    private _get( page: string, params : string ): Promise<any> {
      return this._http
        .get<any>(this._getUrl(page) + "?" + params, this._httpOptions)
        .toPromise();
    }

    private _post( page: string, body : any ): Promise<any> {
      //if(body.method == "ptz.start") this._lastCallBody = body;
      return this._http
        .post<any>(this._getUrl(page), body, this._httpOptions)
        .toPromise();
    }

    private _getUrl(page: string): string {
      return this._urlBase + this._ptz + "/" + page;
    }

    private _login() : Promise<any> {
      let body = `username=${this._user}&password=${this._pass}`;
      //
      this._addLog(this._ptz, "login: " + JSON.stringify(body));
      return this._get("login/login", body).then( r => {
        this._addLog(this._ptz, "login return: " + JSON.stringify(r));
      });
    }

    private _ptzStart(code: string, arg1 = null, arg2 = null, arg3 = null, arg4 = null, channel = null) : Promise<any> {
      if( !this.isConnected() )
        return this._getPromiseRejectWithText(`_ptzStart: ${this._ptz} is not connected`);

      var body = {
        "SysCtrl": {
          "PtzCtrl": {
            "nChanel": 0,
            "szPtzCmd": code,
            "byValue": arg1
          }
        }
      };
      /*
      if( body.params.arg1 == null ) delete body.params.arg1;
      if( body.params.arg2 == null ) delete body.params.arg2;
      if( body.params.arg3 == null ) delete body.params.arg3;
      if( body.params.arg4 == null ) delete body.params.arg4;
      */
      //
      this._addLog(this._ptz, "_ptzStart : " + JSON.stringify(body));
      return this._post("ajaxcom", { "szCmd": JSON.stringify(body) }).then( r => {
        this._addLog(this._ptz, "_ptzStart return: " + JSON.stringify(r));
        this._sessionData.id = r.id;
      });
    }

    public setZoomSpeed(value) : Promise<any> {
      //
      let table = [
        [
          {
            "DigitalZoom": false,
            "Speed": value,
            "ZoomLimit": 4
          },
          {
            "DigitalZoom": false,
            "Speed": 100,
            "ZoomLimit": 4
          },
          {
            "DigitalZoom": false,
            "Speed": 100,
            "ZoomLimit": 4
          }
        ]
      ];
      //
      //let body = this._getConfigManagerSetConfigStructure("VideoInZoom", table);
      //
      return null; //this._systemMulticall([ body ]);
    }

    public connect() : Promise<any> {
      return this._login();
    }

    public loadPreset(id: number, freezeFocus: boolean = false) : Promise<any> {
      if( !this.isConnected() )
        return this._getPromiseRejectWithText(`loadPreset: ${this._ptz} is not connected`);

      return this._ptzStart("preset_call", id);
      /*.then( () => {
        if(freezeFocus)
          setTimeout( () => {
            this.startFocusOut().then(() => {
              this.stopFocusOut() }) }, 5000);
      });*/
    }

    public savePreset(id: number) : Promise<any> {
      return this._ptzStart("SetPreset", id, 0, 0);
    }

    public startZoomIn(amount: number = 5) : Promise<any> {
      return this._ptzStart("ZoomTele", amount, 0, 0);
    }

    public stopZoomIn(amount: number = 5) : Promise<any> {
      return null; //this._ptzStop("ZoomTele", amount, 0, 0);
    }

    public startZoomOut(amount: number = 5) : Promise<any> {
      return this._ptzStart("ZoomWide", amount, 0, 0);
    }

    public stopZoomOut(amount: number = 5) : Promise<any> {
      return null; // this._ptzStop("ZoomWide", amount, 0, 0);
    }

    public startFocusIn(amount: number = 5) : Promise<any> {
      return this._ptzStart("FocusNear", amount, 0, 0);
    }

    public stopFocusIn(amount: number = 5) : Promise<any> {
      return null; // this._ptzStop("FocusNear", amount, 0, 0);
    }

    public startFocusOut(amount: number = 5) : Promise<any> {
      return this._ptzStart("FocusFar", amount, 0, 0);
    }

    public stopFocusOut(amount: number = 5) : Promise<any> {
      return null; // this._ptzStop("FocusFar", amount, 0, 0);
    }

    public startJoystick(direction: string, speed1: number, speed2: number = 0) : Promise<any> {
      return this._ptzStart(direction, speed1, speed2, 0);
    }

    public stopJoystick(direction: string, speed1: number, speed2: number = 0) : Promise<any> {
      return null; // this._ptzStop(direction, speed1, speed2, 0);
    }

    public specificPosition(horizontal: number, vertical: number, zoom: number) : Promise<any> {
      //let degree = -18.99666 + 6.9773 * Math.log(horizontal);
      return this._ptzStart("PositionABS", horizontal, vertical, zoom);
    }

    public stopLastCall() : Promise<any> {
      if( !this.isConnected() )
        return this._getPromiseRejectWithText(`stopLastCall: ${this._ptz} is not connected`);

      if(this._lastCallBody && this._lastCallBody.method)
      {
        if(this._lastCallBody.method == "ptz.start")
        {
          this._lastCallBody.method = "ptz.stop";
          this._lastCallBody.id = this._sessionData.id + 1,
          this._lastCallBody.session = this._sessionData.session,
          this._lastCallBody.object = this._sessionData.result,
          //this._lastCallBody.seq = this._getSeq()
          //
          this._addLog(this._ptz, "stopLastCall : " + JSON.stringify(this._lastCallBody));
          return this._post("RPC2", this._lastCallBody).then( r => {
            this._addLog(this._ptz, "stopLastCall return: " + JSON.stringify(r));
            this._sessionData.id = r.id;
          });
        }
      }

      return Promise.resolve();
    }

}
