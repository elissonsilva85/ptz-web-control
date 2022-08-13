import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { isDevMode } from '@angular/core';
import { Router,NavigationEnd  } from '@angular/router';

import { PtzAbstractSession } from '../class/ptz-abstract-session';
import { PtzConferenceSession } from '../class/ptz-conference-session';
import { PtzDahuaSession } from '../class/ptz-dahua-session';

@Injectable({
  providedIn: 'root'
})
export class RcpService {

  logs : any[] = [];
  keepAliveInterval: number = 9000;
  urlBase: string;

  ptzConnection: any = {};
  ptzSessionList = new Map();
  startStreamingCommands: string[] = [];
  stopStreamingCommands: string[] = [];
  customShortcuts: any[] = [];

  constructor(private _http: HttpClient,
    private _router: Router) {  }

  public loadAppConfig() {
    return this._http.get('/api/config/')
      .toPromise()
      .then( (data: any) => {
        this.urlBase = this._router.url + "api/ptz/operate/";
        this.startStreamingCommands = data.startStreaming;
        this.stopStreamingCommands = data.stopStreaming;
        this.ptzConnection = data.ptz.connection;
        this.customShortcuts = data.shortcuts;

        console.log("devMode:", isDevMode());
      })
      .catch(err => {
        throw Error(`Config file not loaded! ${err}`);
      });
  }

  public getSession(ptz: string) : PtzAbstractSession {
    if(this.ptzSessionList.get(ptz) == null)
      switch(this.ptzConnection[ptz].brand) {
        case "dahua": {
          this.ptzSessionList.set(ptz, new PtzDahuaSession(
            this._http,
            ptz,
            this.urlBase,
            this.ptzConnection[ptz].user,
            this.ptzConnection[ptz].password,
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
            this.ptzConnection[ptz].user,
            this.ptzConnection[ptz].password,
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
          throw new Error(`Brand ${this.ptzConnection[ptz].brand} not recognized`);

      }

    return this.ptzSessionList.get(ptz);
  }

  public getStatusSvg(ptz : string) : string {
    let isConnected = true;
    return ( isConnected ? "led-green-black.svg" : "led-red-black.svg" );
  }

  public clearLog() {
    this.logs = [];
  }

  public simplePost( page: string, body : any ): Promise<any> {
    return this._http
      .post<any>(this.urlBase + page, body)
      .toPromise();
  }

  public simpleGet( page: string, params : string ): Promise<any> {
    return this._http
      .get<any>(this.urlBase + page + "?" + params)
      .toPromise();
  }

}
