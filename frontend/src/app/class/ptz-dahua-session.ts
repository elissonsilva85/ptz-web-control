import { DahuaFaultylabs } from './dahua-faultylabs';
import { DahuaParamRequestSetConfig } from './dahua-param-request-setConfig';
import { DahuaParamRequestSetConfigVideoColorTable } from './dahua-param-request-setConfig-VideoColorTable';
import { DahuaParamRequestSetConfigVideoInMode } from './dahua-param-request-setConfig-VideoInMode';
import { DahuaParamRequestSetConfigVideoInWhiteBalance } from './dahua-param-request-setConfig-VideoInWhiteBalance';
import { DahuaSessionData } from './dahua-session-data';
import { PtzAbstractSession } from './ptz-abstract-session';

const faultylabs: DahuaFaultylabs = new DahuaFaultylabs();

export class PtzDahuaSession extends PtzAbstractSession {

    private _sessionData : DahuaSessionData = new DahuaSessionData();

    ///////////// UNDER DEVELOPMENT //////////////

    public setConfig(list: any[], table: any[]) : Promise<any> {
      let body: DahuaParamRequestSetConfig[] = list.map( (name, i) => { 
        return new DahuaParamRequestSetConfig(name, table[i]);
      });
  
      //
      this._addLog(this._ptz, "setConfig : " + JSON.stringify(body));
      return this._post("dahua/setConfig", body).then( r => {
        this._addLog(this._ptz, "setConfig return: " + JSON.stringify(r));
        return r; 
      });
    }

    public setTemporaryConfig(name: string, table: any) : Promise<any> {
      let body: DahuaParamRequestSetConfig = new DahuaParamRequestSetConfig(name, table);
  
      //
      this._addLog(this._ptz, "setTemporaryConfig : " + JSON.stringify(body));
      return this._post("dahua/setTemporaryConfig", body).then( r => {
        this._addLog(this._ptz, "setTemporaryConfig return: " + JSON.stringify(r));
        return r; 
      });
    }
    
    public getConfig(list: any[]) : Promise<any> {
      //
      this._addLog(this._ptz, "getConfig : " + list.join(","));
      return this._get("dahua/getConfig/" + list.join(","), "").then( r => {
        this._addLog(this._ptz, "getConfig return: " + JSON.stringify(r));
        return r; 
      });

      /*
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
      */
    }

    public setVideoColor(videoColorTable: DahuaParamRequestSetConfigVideoColorTable[]) : Promise<any> {
      return this.setTemporaryConfig("VideoColor", videoColorTable);
    }

    public setVideoInWhiteBalance(videoInWhiteBalanceTable: DahuaParamRequestSetConfigVideoInWhiteBalance[]) : Promise<any> {
      return this.setTemporaryConfig("VideoInWhiteBalance", videoInWhiteBalanceTable);
    }

    public setVideoInMode(videoInMode: DahuaParamRequestSetConfigVideoInMode) : Promise<any> {
      return this.setTemporaryConfig("VideoInMode", videoInMode);
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
