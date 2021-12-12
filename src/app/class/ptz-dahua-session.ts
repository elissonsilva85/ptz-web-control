import { DahuaFaultylabs } from './dahua-faultylabs';
import { DahuaSessionData } from './dahua-session-data';
import { PtzAbstractSession } from './ptz-abstract-session';

const faultylabs: DahuaFaultylabs = new DahuaFaultylabs();

export class PtzDahuaSession extends PtzAbstractSession {

    private _keepAliveInterval: number = 9000;
    private _sessionData : DahuaSessionData = new DahuaSessionData();

    protected _post( page: string, body : any ): Promise<any> {
      if(body.method == "ptz.start") this._lastCallBody = body;
      return super._post(page, body);
    }

    /////// PRIVATE METHODS /////////////////////

    private _getSeq(): number {
      let b = 0;
      let a = this._sessionData.session.toString().replace(/[^0-9]/gi, "")
        , c = parseInt(a).toString(2).slice(-24)
        , d = ("00000000" + b.toString(2)).slice(-8);
      return b = (b + 1) % 256,
      parseInt(c + d, 2)
    }

    private _getHashPassword() : string {
      let pass = this._hex_md5(
        this._user + ":" +
        this._sessionData.random + ":" +
        this._hex_md5(this._user + ":" +
        this._sessionData.realm + ":" +
        this._pass)).toUpperCase();
      return pass;
    }

    private _hex_md5(text: string): string {
      return faultylabs.MD5(text);
    }

    private _startSession() : Promise<any> {
      clearTimeout(this._sessionData.timer);

      let body = {
        "method": "global.login",
        "params": {
          "userName": this._user,
          "password": "",
          "clientType": "Web3.0",
          "loginType": "Direct"
        },
        "id": 4
      };
      //
      this._addLog(this._ptz, "getSession: " + JSON.stringify(body));
      return this._post( "RPC2_Login", body).then( r => {
        this._addLog(this._ptz, "getSession return: " + JSON.stringify(r));
        this._sessionData.random = r.params.random;
        this._sessionData.realm = r.params.realm;
        this._sessionData.session = r.session;
        this._sessionData.isConnected = true;
        //
        this._keepAliveTimeout();
        //
      }).catch( (error) => {
        this._sessionData.isConnected = false;
        throw error;
      } );
    }

    private _login() : Promise<any> {
      let hashPass = this._getHashPassword();
      let body = {
        "method": "global.login",
        "params": {
          "userName": this._user,
          "password": hashPass,
          "clientType": "Web3.0",
          "loginType": "Direct",
          "authorityType": "Default"
        },
        "id":3,
        "session": this._sessionData.session
      };
      //
      this._addLog(this._ptz, "login: " + JSON.stringify(body));
      return this._post("RPC2_Login", body).then( r => {
        // TODO
        // Validate login return (check result value)
        // ERROR: {"error":{"code":268632085,"message":"Component error: User or password not valid!"},"id":3,"params":{"remainLockSecond":0,"remainLoginTimes":2},"result":false,"session":2147483643}
        // SUCCESS: {"id":3,"params":{"keepAliveInterval":60},"result":true,"session":"4f8fc4ebe6ab2b4b6a04909b3fb91686"}
        this._addLog(this._ptz, "login return: " + JSON.stringify(r));
        this._sessionData.session = r.session;
        this._sessionData.id = r.id;
      });
    }

    private _factoryInstance() : Promise<any> {
      var body = {
        "method":"ptz.factory.instance",
        "params":{
          "channel":0
        },
        "id": this._sessionData.id + 1,
        "session": this._sessionData.session
      };
      //
      this._addLog(this._ptz, "factoryInstance : " + JSON.stringify(body));
      return this._post("RPC2", body).then( r => {
        this._addLog(this._ptz, "factoryInstance return: " + JSON.stringify(r));
        this._sessionData.result = r.result;
        this._sessionData.id = r.id;
      });
    }

    private _keepAlive() : Promise<any> {
      if( !this.isConnected() )
        return this._getPromiseRejectWithText(`_keepAlive: ${this._ptz} is not connected`);

      var body = {
        "method":"global.keepAlive",
        "params":{
          "timeout":10000,
          "active": true
        },
        "id": this._sessionData.id + 1,
        "session": this._sessionData.session
      };
      //
      this._addLog(this._ptz, "keepAlive : " + JSON.stringify(body));
      return this._post("RPC2", body).then( r => {
        this._addLog(this._ptz, "keepAlive return: " + JSON.stringify(r));
        this._sessionData.id = r.id;
      });
    }

    private _keepAliveTimeout() {
      this._sessionData.timer = setTimeout(() => {
        this._keepAlive().then( () => { this._keepAliveTimeout(); })
      }, this._keepAliveInterval);
    }

    private _ptzStart(code: string, arg1 = null, arg2 = null, arg3 = null, arg4 = null, channel = null) : Promise<any> {
      if( !this.isConnected() )
        return this._getPromiseRejectWithText(`_ptzStart: ${this._ptz} is not connected`);

      var body = {
        "method": "ptz.start",
        "params": {
          "code": code,
          "arg1": arg1,
          "arg2": arg2,
          "arg3": arg3,
          "arg4": arg4
        },
        "id": this._sessionData.id + 1,
        "session": this._sessionData.session,
        "object": this._sessionData.result,
        "seq": this._getSeq()
      };
      if( body.params.arg1 == null ) delete body.params.arg1;
      if( body.params.arg2 == null ) delete body.params.arg2;
      if( body.params.arg3 == null ) delete body.params.arg3;
      if( body.params.arg4 == null ) delete body.params.arg4;
      //
      this._addLog(this._ptz, "_ptzStart : " + JSON.stringify(body));
      return this._post("RPC2", body).then( r => {
        this._addLog(this._ptz, "_ptzStart return: " + JSON.stringify(r));
        this._sessionData.id = r.id;
      });
    }

    private _ptzStop(code: string, arg1 = null, arg2 = null, arg3 = null, arg4 = null, channel = null) : Promise<any> {
      if( !this.isConnected() )
        return this._getPromiseRejectWithText(`_ptzStop: ${this._ptz} is not connected`);

      var body = {
        "method": "ptz.stop",
        "params": {
          "code": code,
          "arg1": arg1,
          "arg2": arg2,
          "arg3": arg3,
          "arg4": arg4
        },
        "id": this._sessionData.id + 1,
        "session": this._sessionData.session,
        "object": this._sessionData.result,
        "seq": this._getSeq()
      };
      if( body.params.arg1 == null ) delete body.params.arg1;
      if( body.params.arg2 == null ) delete body.params.arg2;
      if( body.params.arg3 == null ) delete body.params.arg3;
      if( body.params.arg4 == null ) delete body.params.arg4;
      //
      this._addLog(this._ptz, "_ptzStop : " + JSON.stringify(body));
      return this._post("RPC2", body).then( r => {
        this._addLog(this._ptz, "_ptzStop return: " + JSON.stringify(r));
        this._sessionData.id = r.id;
      });
    }

    private _systemMulticall(params: any[]) : Promise<any> {
      if( !this.isConnected() )
        return this._getPromiseRejectWithText(`_systemMulticall: ${this._ptz} is not connected`);

      var body = {
        "method": "system.multicall",
        "params": params,
        "id": this._sessionData.id + 1,
        "session": this._sessionData.session
      };
      //
      this._addLog(this._ptz, "_systemMulticall : " + JSON.stringify(body));
      return this._post("RPC2", body).then( r => {
        this._addLog(this._ptz, "_systemMulticall return: " + JSON.stringify(r));
        this._sessionData.id = r.id;
      });
    }

    private _getConfigManagerSetConfigStructure(name: string, table: any[], options: any[] = []) {
      var body = {
        "method": "configManager.setConfig",
        "params": {
          "name": name,
          "table": table,
          "options": options
        },
        "id": this._sessionData.id + 1,
        "session": this._sessionData.session
      };
      //
      return body;
    }

    /////// PUBLIC METHODS ///////////

    public isConnected() : boolean {
      return this._sessionData.isConnected;
    }

    public connect() : Promise<any> {
      return this._startSession()
        .then( () => { return this._login(); })
        .then( () => { return this._factoryInstance(); })
    }

    public loadPreset(id: number) : Promise<any> {
      if( !this.isConnected() )
        return this._getPromiseRejectWithText(`loadPreset: ${this._ptz} is not connected`);

      let freezeFocus = false;

      return this._ptzStart("GotoPreset", id, 0, 0).then( () => {
        if(freezeFocus)
          setTimeout( () => {
            this.startFocusOut().then(() => {
              this.stopFocusOut() }) }, 5000);
      });
    }

    public savePreset(id: number) : Promise<any> {
      return this._ptzStart("SetPreset", id, 0, 0);
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
      let body = this._getConfigManagerSetConfigStructure("VideoInZoom", table);
      //
      return this._systemMulticall([ body ]);
    }

    public startZoomIn() : Promise<any> {
      let amount = 5;
      return this._ptzStart("ZoomTele", amount, 0, 0);
    }

    public stopZoomIn() : Promise<any> {
      let amount = 5;
      return this._ptzStop("ZoomTele", amount, 0, 0);
    }

    public startZoomOut() : Promise<any> {
      let amount = 5;
      return this._ptzStart("ZoomWide", amount, 0, 0);
    }

    public stopZoomOut() : Promise<any> {
      let amount = 5;
      return this._ptzStop("ZoomWide", amount, 0, 0);
    }

    public startFocusIn() : Promise<any> {
      let amount = 5;
      return this._ptzStart("FocusNear", amount, 0, 0);
    }

    public stopFocusIn() : Promise<any> {
      let amount = 5;
      return this._ptzStop("FocusNear", amount, 0, 0);
    }

    public startFocusOut() : Promise<any> {
      let amount = 5;
      return this._ptzStart("FocusFar", amount, 0, 0);
    }

    public stopFocusOut() : Promise<any> {
      let amount = 5;
      return this._ptzStop("FocusFar", amount, 0, 0);
    }

    public startJoystick(direction: string, speed1: number, speed2: number = 0) : Promise<any> {
      return this._ptzStart(direction, speed1, speed2, 0);
    }

    public stopJoystick(direction: string, speed1: number, speed2: number = 0) : Promise<any> {
      return this._ptzStop(direction, speed1, speed2, 0);
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
          this._lastCallBody.seq = this._getSeq()
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


    ///////////// UNDER DEVELOPMENT //////////////

    public specificPosition(horizontal: number, vertical: number, zoom: number) : Promise<any> {
      //let degree = -18.99666 + 6.9773 * Math.log(horizontal);
      return this._ptzStart("PositionABS", horizontal, vertical, zoom);
    }

    public getConfig(list: any[]) : Promise<any> {
      if( !this.isConnected() )
        return this._getPromiseRejectWithText(`getConfig: ${this._ptz} is not connected`);

      var body = {
        "method": "system.multicall",
        "params": [ ],
        "id": 0,
        "session": this._sessionData.session
      };

      body.params = list.map( (name, i) => { return {
          "method": "configManager.getConfig",
          "params": {
            "name": name
          },
          "id": this._sessionData.id + 1 + i,
          "session": this._sessionData.session
        }
      });

      body.id = this._sessionData.id + 1 + list.length;

      //
      this._addLog(this._ptz, "getConfig : " + JSON.stringify(body));
      return this._post("RPC2", body).then( r => {
        this._addLog(this._ptz, "getConfig return: " + JSON.stringify(r));
        this._sessionData.id = r.id;
        return r; 
      });
    }

    public setConfig(list: any[], table: any[]) : Promise<any> {
      if( !this.isConnected() )
        return this._getPromiseRejectWithText(`setConfig: ${this._ptz} is not connected`);

      var body: any = {};

      if(list.length == 1) {

        body = {
          "method": "configManager.setConfig",
          "params": {
            "name": list[0],
            "table": [
              table[0]
            ],
            "options": []
          },
          "id": this._sessionData.id + 1,
          "session": this._sessionData.session
        };

      }
      else {
        body = {  
          "method": "system.multicall",
          "params": [ ],
          "id": 0,
          "session": this._sessionData.session
        };

        body.params = list.map( (name, i) => { return {
            "method": "configManager.setConfig",
            "params": {
              "name": name,
              "table": [ table[i] ],
              "options": []
            },
            "id": this._sessionData.id + 1 + i,
            "session": this._sessionData.session
          }
        });

        body.id = this._sessionData.id + 1 + list.length;
      }

      //
      this._addLog(this._ptz, "setConfig : " + JSON.stringify(body));
      return this._post("RPC2", body).then( r => {
        this._addLog(this._ptz, "setConfig return: " + JSON.stringify(r));
        this._sessionData.id = r.id;
        return r; 
      });
    }

    public setVideoColor(videoColorTable: any[]) : Promise<any> {
      if( !this.isConnected() )
        return this._getPromiseRejectWithText(`setVideoColor: ${this._ptz} is not connected`);

      var body = {
        "method": "configManager.setTemporaryConfig",
        "params": {
          "name": "VideoColor",
          "table": videoColorTable,
          "options": []
        },
        "id": this._sessionData.id + 1,
        "session": this._sessionData.session
      };
      //
      this._addLog(this._ptz, "setVideoColor : " + JSON.stringify(body));
      return this._post("RPC2", body).then( r => {
        this._addLog(this._ptz, "setVideoColor return: " + JSON.stringify(r));
        this._sessionData.id = r.id;
      });
    }

    public setVideoInMode(config: number) : Promise<any> {
      if( !this.isConnected() )
        return this._getPromiseRejectWithText(`setVideoInMode: ${this._ptz} is not connected`);

      var body = {
        "method": "configManager.setTemporaryConfig",
        "params": {
          "name": "VideoInMode",
          "table": [
            {
              "Config": [
                config
              ],
              "Mode": 0,
              "TimeSection": [
                [
                  "0 00:00:00-24:00:00",
                  "0 00:00:00-24:00:00",
                  "0 00:00:00-24:00:00",
                  "0 00:00:00-24:00:00",
                  "0 00:00:00-24:00:00",
                  "0 00:00:00-24:00:00"
                ],
                [
                  "0 00:00:00-24:00:00",
                  "0 00:00:00-24:00:00",
                  "0 00:00:00-24:00:00",
                  "0 00:00:00-24:00:00",
                  "0 00:00:00-24:00:00",
                  "0 00:00:00-24:00:00"
                ],
                [
                  "0 00:00:00-24:00:00",
                  "0 00:00:00-24:00:00",
                  "0 00:00:00-24:00:00",
                  "0 00:00:00-24:00:00",
                  "0 00:00:00-24:00:00",
                  "0 00:00:00-24:00:00"
                ],
                [
                  "0 00:00:00-24:00:00",
                  "0 00:00:00-24:00:00",
                  "0 00:00:00-24:00:00",
                  "0 00:00:00-24:00:00",
                  "0 00:00:00-24:00:00",
                  "0 00:00:00-24:00:00"
                ],
                [
                  "0 00:00:00-24:00:00",
                  "0 00:00:00-24:00:00",
                  "0 00:00:00-24:00:00",
                  "0 00:00:00-24:00:00",
                  "0 00:00:00-24:00:00",
                  "0 00:00:00-24:00:00"
                ],
                [
                  "0 00:00:00-24:00:00",
                  "0 00:00:00-24:00:00",
                  "0 00:00:00-24:00:00",
                  "0 00:00:00-24:00:00",
                  "0 00:00:00-24:00:00",
                  "0 00:00:00-24:00:00"
                ],
                [
                  "0 00:00:00-24:00:00",
                  "0 00:00:00-24:00:00",
                  "0 00:00:00-24:00:00",
                  "0 00:00:00-24:00:00",
                  "0 00:00:00-24:00:00",
                  "0 00:00:00-24:00:00"
                ]
              ]
            }
          ],
          "options": []
        },
        "id": this._sessionData.id + 1,
        "session": this._sessionData.session
      };
      //
      this._addLog(this._ptz, "setVideoInMode : " + JSON.stringify(body));
      return this._post("RPC2", body).then( r => {
        this._addLog(this._ptz, "setVideoInMode return: " + JSON.stringify(r));
        this._sessionData.id = r.id;
      });
    }

    public setVideoInWhiteBalance(videoInWhiteBalanceTable: any[]) : Promise<any> {
      if( !this.isConnected() )
        return this._getPromiseRejectWithText(`setVideoInWhiteBalance: ${this._ptz} is not connected`);

      var body = {
        "method": "configManager.setTemporaryConfig",
        "params": {
          "name": "VideoInWhiteBalance",
          "table": [
            videoInWhiteBalanceTable
          ],
          "options": []
        },
        "id": this._sessionData.id + 1,
        "session": this._sessionData.session
      };
      //
      this._addLog(this._ptz, "setVideoInWhiteBalance : " + JSON.stringify(body));
      return this._post("RPC2", body).then( r => {
        this._addLog(this._ptz, "setVideoInWhiteBalance return: " + JSON.stringify(r));
        this._sessionData.id = r.id;
      });
    }

    public moveDirectly(coord: number[], speed: number) : Promise<any> {
      if( !this.isConnected() )
        return this._getPromiseRejectWithText(`moveDirectly: ${this._ptz} is not connected`);

      var body = {
        "method": "ptz.moveDirectly",
        "params": {
          "screen": coord,
          "speed": speed
        },
        "id": this._sessionData.id + 1,
        "session": this._sessionData.session,
        "object": this._sessionData.result
      };
      //
      this._addLog(this._ptz, "moveDirectly : " + JSON.stringify(body));
      return this._post("RPC2", body).then( r => {
        this._addLog(this._ptz, "moveDirectly return: " + JSON.stringify(r));
        this._sessionData.session = r.session;
        this._sessionData.id = r.id;
      });
    }

}
