import { DahuaFaultylabs } from './dahua-faultylabs';
import { DahuaSessionData } from './dahua-session-data';
import { PtzAbstractSession } from './ptz-abstract-session';

const faultylabs: DahuaFaultylabs = new DahuaFaultylabs();

export class PtzDahuaSession extends PtzAbstractSession {

    private _sessionData : DahuaSessionData = new DahuaSessionData();

    ///////////// UNDER DEVELOPMENT //////////////

    public setConfig(list: any[], table: any[]) : Promise<any> {
      let body: any = list.map( (name, i) => { 
        return {
            "name": name,
            "table": [ table[i] ],
            "options": []
          }
      });
  
      //
      this._addLog(this._ptz, "setConfig : " + JSON.stringify(body));
      return this._post("config", body).then( r => {
        this._addLog(this._ptz, "setConfig return: " + JSON.stringify(r));
        return r; 
      });
    }
    
    public getConfig(list: any[]) : Promise<any> {
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
