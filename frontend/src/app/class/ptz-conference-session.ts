import { PtzAbstractSession } from './ptz-abstract-session';

export class PtzConferenceSession extends PtzAbstractSession {

    private _isConnected : boolean = false;


    protected _post( page: string, body : any ): Promise<any> {
      return super._post(page, body);
    }

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
      this._lastCallBody = body;
      this._addLog(this._ptz, "_szCmd : " + JSON.stringify(body));
      return this._post("ajaxcom", `szCmd=${JSON.stringify(body)}`      ).then( r => {
        this._addLog(this._ptz, "_szCmd return: " + JSON.stringify(r));
      });
    }

    private _login() : Promise<any> {
      //
      let body = `username=${this._user}&password=${this._pass}`;
      //
      this._addLog(this._ptz, "_login");
      return this._get("login/login", body).then( r => {
        this._isConnected = true;
        this._addLog(this._ptz, "_login return: " + JSON.stringify(r));
      });
    }

    private _conferenceDirection(direction: string, speed1: number, speed2: number): string {
      let conferenceDirection = "";
      switch(direction)
      {
        case "LeftUp": {
          if(speed2 == 0 && speed1 > 0) conferenceDirection = "up";
          else if(speed2 > 0 && speed1 == 0) conferenceDirection = "left";
          else conferenceDirection = "leftup";
          break;
        }
        case "LeftDown": {
          if(speed2 == 0 && speed1 > 0) conferenceDirection = "down";
          else if(speed2 > 0 && speed1 == 0) conferenceDirection = "left";
          else conferenceDirection = "leftdown";
          break;
        }
        case "RightUp": {if(speed2 == 0 && speed1 > 0) conferenceDirection = "up";
          else if(speed2 > 0 && speed1 == 0) conferenceDirection = "right";
          else conferenceDirection = "rightup";
          break;
        }
        case "RightDown": {if(speed2 == 0 && speed1 > 0) conferenceDirection = "down";
          else if(speed2 > 0 && speed1 == 0) conferenceDirection = "right";
          else conferenceDirection = "rightdown";
          break;
        }
      }

      return conferenceDirection;
    }

    private _conferenceSpeed(speed: number) : number {
      switch(speed)
      {
        case 1 : return 0;
        case 2 : return 20;
        case 3 : return 40;
        case 4 : return 40;
        case 5 : return 50;
        case 6 : return 50;
        case 7 : return 100;
        case 8 : return 100;
      }
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
      if( !this.isConnected() )
        return this._getPromiseRejectWithText(`savePreset: ${this._ptz} is not connected`);

      return this._szCmd("preset_set", id);
    }

    public setZoomSpeed(value) : Promise<any> {
      return Promise.resolve();
    }

    public startZoomIn() : Promise<any> {
      if( !this.isConnected() )
        return this._getPromiseRejectWithText(`startZoomIn: ${this._ptz} is not connected`);

      return this._szCmd(`zoomadd_start`, 0);
    }

    public stopZoomIn() : Promise<any> {
      if( !this.isConnected() )
        return this._getPromiseRejectWithText(`stopZoomIn: ${this._ptz} is not connected`);

      return this._szCmd(`zoomadd_stop`, 0);
    }

    public startZoomOut() : Promise<any> {
      if( !this.isConnected() )
        return this._getPromiseRejectWithText(`startZoomOut: ${this._ptz} is not connected`);

      return this._szCmd(`zoomdec_start`, 0);
    }

    public stopZoomOut() : Promise<any> {
      if( !this.isConnected() )
        return this._getPromiseRejectWithText(`stopZoomOut: ${this._ptz} is not connected`);

      return this._szCmd(`zoomdec_stop`, 0);
    }

    public startFocusIn() : Promise<any> {
      if( !this.isConnected() )
        return this._getPromiseRejectWithText(`startFocusIn: ${this._ptz} is not connected`);

      return this._szCmd(`focusadd_start`, 0);
    }

    public stopFocusIn() : Promise<any> {
      if( !this.isConnected() )
        return this._getPromiseRejectWithText(`stopFocusIn: ${this._ptz} is not connected`);

      return this._szCmd(`focusadd_stop`, 0);
    }

    public startFocusOut() : Promise<any> {
      if( !this.isConnected() )
        return this._getPromiseRejectWithText(`startFocusOut: ${this._ptz} is not connected`);

      return this._szCmd(`focusdec_start`, 0);
    }

    public stopFocusOut() : Promise<any> {
      if( !this.isConnected() )
        return this._getPromiseRejectWithText(`stopFocusOut: ${this._ptz} is not connected`);

      return this._szCmd(`focusdec_stop`, 0);
    }

    public startJoystick(direction: string, speed1: number, speed2: number) : Promise<any> {
      if( !this.isConnected() )
        return this._getPromiseRejectWithText(`startJoystick: ${this._ptz} is not connected`);

      if( (speed1 + speed2) == 0 )
        return Promise.resolve();

      let conferenceDirection = this._conferenceDirection(direction, speed1, speed2);
      let conferenceSpeed = this._conferenceSpeed(Math.max(speed1, speed2));

      return this._szCmd(`${conferenceDirection}_start`, conferenceSpeed);
    }

    public stopJoystick(direction: string, speed1: number, speed2: number) : Promise<any> {
      if( !this.isConnected() )
        return this._getPromiseRejectWithText(`stopJoystick: ${this._ptz} is not connected`);

      let conferenceDirection = this._conferenceDirection(direction, speed1, speed2);
      let conferenceSpeed = this._conferenceSpeed(Math.max(speed1, speed2));

      return this._szCmd(`${conferenceDirection}_stop`, conferenceSpeed);
    }

    public stopLastCall() : Promise<any> {
      console.log("stopLastCall", this._lastCallBody);

      if( !this.isConnected() )
        return this._getPromiseRejectWithText(`stopLastCall: ${this._ptz} is not connected`);

      if(this._lastCallBody.SysCtrl?.PtzCtrl?.szPtzCmd)
      {
        let szPtzCmd = this._lastCallBody.SysCtrl.PtzCtrl.szPtzCmd;
        let byValue  = this._lastCallBody.SysCtrl.PtzCtrl.byValue;
        console.log("szPtzCmd", szPtzCmd);

        if(szPtzCmd.includes("start"))
        {
          szPtzCmd = szPtzCmd.replace("start", "stop");
          return this._szCmd(szPtzCmd, byValue);
        }
      }

      return Promise.resolve();
    }

    ///////////// UNDER DEVELOPMENT //////////////

    public specificPosition(horizontal: number, vertical: number, zoom: number) : Promise<any> {
      return Promise.resolve();
    }

    public getVideoColor(table: any[]) : Promise<any> {
      return Promise.resolve();
    }

    public setVideoColor(table: any[]) : Promise<any> {
      return Promise.resolve();
    }

    public moveDirectly(coord: number[], speed: number) : Promise<any> {
      return Promise.resolve();
    }

}
