import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { isDevMode } from '@angular/core';
import { PtzAbstractSession } from '../class/ptz-abstract-session';
import { PtzConferenceSession } from '../class/ptz-conference-session';
import { PtzDahuaSession } from '../class/ptz-dahua-session';

@Injectable({
  providedIn: 'root'
})
export class RcpService {

  logs : any[] = [];
  keepAliveInterval: number = 9000;
  urlBase: string = "http://localhost:4200/app/";

  ptzUserPass: any = {};
  ptzBrand: string = "";
  ptzCodes: string[] = [];
  ptzSessionList = new Map();
  startStreamingCommands: string[] = [];
  stopStreamingCommands: string[] = [];

  constructor(private _http: HttpClient) {  }

  public loadAppConfig() {
    return this._http.get('/app/assets/config.json')
      .toPromise()
      .then( (data: any) => {
        this.urlBase = data.urlBase;
        this.ptzBrand = data.ptzBrand;
        this.ptzCodes = data.ptzCodes;
        this.startStreamingCommands = data.startStreaming;
        this.stopStreamingCommands = data.stopStreaming;
        this.ptzCodes.forEach( (ptz) => {
          this.ptzUserPass[ptz] = data[ptz];
        });

        console.log("devMode:", isDevMode());
        if(isDevMode()) this.urlBase = "http://localhost:4200/app/";
      })
      .catch(err => {
        throw Error(`Config file not loaded! ${err}`);
      });
  }

  public getSession(ptz: string) : PtzAbstractSession {
    if(this.ptzSessionList.get(ptz) == null)
      switch(this.ptzBrand) {
        case "dahua": {
          this.ptzSessionList.set(ptz, new PtzDahuaSession(
            this._http,
            ptz,
            this.urlBase,
            this.ptzUserPass[ptz].user,
            this.ptzUserPass[ptz].password,
            (ptz : string, text : string) => {
              this.logs.unshift({
                date: new Date(),
                ptz: ptz,
                msg: text });
            }
          ))
          break;
        }

        case "conference": {
          this.ptzSessionList.set(ptz, new PtzConferenceSession(
            this._http,
            ptz,
            this.urlBase,
            this.ptzUserPass[ptz].user,
            this.ptzUserPass[ptz].password,
            (ptz : string, text : string) => {
              this.logs.unshift({
                date: new Date(),
                ptz: ptz,
                msg: text });
            }
          ))
          break;
        }

        default:
          throw new Error(`Brand ${this.ptzBrand} not recognized`);

      }

    return this.ptzSessionList.get(ptz);
  }

  public getStatusSvg(ptz : string) : string {
    let isConnected = this.getSession(ptz).isConnected();
    return ( isConnected ? "led-green-black.svg" : "led-red-black.svg" );
  }

  public clearLog() {
    this.logs = [];
  }

}
