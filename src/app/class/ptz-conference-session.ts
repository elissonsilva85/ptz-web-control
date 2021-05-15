import { PtzAbstractSession } from './ptz-abstract-session';

export class PtzConferenceSession extends PtzAbstractSession {

    private _isConnected : boolean = false;

    private _szCmd(szPtzCmd: string, arg1 = null, arg2 = null, arg3 = null, arg4 = null, channel = null) : Promise<any> {
      if( !this.isConnected() )
        return this._getPromiseRejectWithText(`_szCmd: ${this._ptz} is not connected`);

      var body = {
        "SysCtrl": {
          "PtzCtrl": {
            "nChanel": 0,
            "szPtzCmd": szPtzCmd,
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
      return this._post("ajaxcom", `szCmd=${JSON.stringify(body)}`      ).then( r => {
        this._addLog(this._ptz, "_ptzStart return: " + JSON.stringify(r));
      });
    }

    private _login() : Promise<any> {
      //
      console.log("this", this);
      //
      let body = `username=${this._user}&password=${this._pass}`;
      //
      this._addLog(this._ptz, "_login: " + JSON.stringify(body));
      return this._get("login/login", body).then( r => {
        this._addLog(this._ptz, "login return: " + JSON.stringify(r));
      });
    }

    /////// PUBLIC METHODS ///////////

    public isConnected() : boolean {
      return this._isConnected;
    }

    public connect() : Promise<any> {
      return this._login();
    }

    public loadPreset(id: number) : Promise<any> {
      if( !this.isConnected() )
        return this._getPromiseRejectWithText(`loadPreset: ${this._ptz} is not connected`);

      return this._szCmd("preset_call", id);
    }

    public savePreset(id: number) : Promise<any> {
      return null;
    }

    public setZoomSpeed(value) : Promise<any> {
      return null;
    }

    public startZoomIn() : Promise<any> {
      return null;
    }

    public stopZoomIn() : Promise<any> {
      return null;
    }

    public startZoomOut() : Promise<any> {
      return null;
    }

    public stopZoomOut() : Promise<any> {
      return null;
    }

    public startFocusIn() : Promise<any> {
      return null;
    }

    public stopFocusIn() : Promise<any> {
      return null;
    }

    public startFocusOut() : Promise<any> {
      return null;
    }

    public stopFocusOut() : Promise<any> {
      return null;
    }

    public startJoystick(direction: string, speed1: number, speed2: number = 0) : Promise<any> {
      return null;
    }

    public stopJoystick(direction: string, speed1: number, speed2: number = 0) : Promise<any> {
      return null;
    }

    public stopLastCall() : Promise<any> {
      if( !this.isConnected() )
        return this._getPromiseRejectWithText(`stopLastCall: ${this._ptz} is not connected`);

      if(this._lastCallBody && this._lastCallBody.method)
      {

      }

      return null;
    }

    ///////////// UNDER DEVELOPMENT //////////////

    public specificPosition(horizontal: number, vertical: number, zoom: number) : Promise<any> {
      return null;
    }

    public getVideoColor(table: any[]) : Promise<any> {
      return null;
    }

    public setVideoColor(table: any[]) : Promise<any> {
      return null;
    }

    public moveDirectly(coord: number[], speed: number) : Promise<any> {
      return null;
    }

}
