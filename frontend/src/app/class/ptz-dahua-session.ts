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
    
    private _keepAlive() : Promise<any> {
      if( !this.isConnected() )
        return this._getPromiseRejectWithText(`_keepAlive: ${this._ptz} is not connected`);

      return this._post("dahua/keepAlive", "").then( r => {
        this._addLog(this._ptz, "keepAlive return: " + JSON.stringify(r));
      });
    }

    private _keepAliveTimeout() {
      this._sessionData.timer = setTimeout(() => {
        this._keepAlive().then( () => { this._keepAliveTimeout(); })
      }, this._keepAliveInterval);
    }

    /////// PUBLIC METHODS ///////////

    public loadPreset(id: number) : Promise<any> {

      let freezeFocus = false;

      return super.loadPreset(id).then( () => {
        if(freezeFocus)
          setTimeout( () => {
            this.startFocusOut().then(() => {
              this.stopFocusOut() }) }, 5000);
      });

    }

    public setZoomSpeed(value) : Promise<any> {
      //
      return this.setConfig([ "VideoInZoom" ], [ [
            {
              "DigitalZoom": false,
              "Speed": value,
              "ZoomLimit": 4
            },
            {
              "DigitalZoom": false,
              "Speed": value,
              "ZoomLimit": 4
            },
            {
              "DigitalZoom": false,
              "Speed": value,
              "ZoomLimit": 4
            }
          ] ]);
      //
    }

    ///////////// UNDER DEVELOPMENT //////////////

    public specificPosition(horizontal: number, vertical: number, zoom: number) : Promise<any> {
      //let degree = -18.99666 + 6.9773 * Math.log(horizontal);
      // return this._ptzStart("PositionABS", horizontal, vertical, zoom);
      return this._getPromiseRejectWithText(`under construction`); 
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
