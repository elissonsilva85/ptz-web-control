import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { isDevMode } from '@angular/core';
import { RcpSession } from '../class/rcp-session';

@Injectable({
  providedIn: 'root'
})
export class RcpService {

  logs : any[] = [];
  keepAliveInterval: number = 9000;
  urlBase: string = "http://localhost:4200/app/";

  ptzUserPass: any = {};
  ptzCodes: string[] = [];
  ptzSessionList = new Map();

  constructor(private _http: HttpClient) {  }

  public loadAppConfig() {
    return this._http.get('/app/assets/config.json')
      .toPromise()
      .then( (data: any) => {
        this.urlBase = data.urlBase;
        this.ptzCodes = data.ptzCodes;
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

  public getSession(ptz: string) : RcpSession {
    if(this.ptzSessionList.get(ptz) == null)
      this.ptzSessionList.set(ptz, new RcpSession(
        this._http,
        ptz,
        this.urlBase,
        this.ptzUserPass[ptz].user,
        this.ptzUserPass[ptz].pass,
        (ptz : string, text : string) => {
          this.logs.unshift({ 
            date: new Date(), 
            ptz: ptz,
            msg: text });
        }
      ))

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
