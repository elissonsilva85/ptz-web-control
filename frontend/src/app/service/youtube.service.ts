import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class YoutubeService {

  constructor(private _http: HttpClient) {  }

  public isConnected() {
    return this._http.get('/api/youtube/isConnected')
      .toPromise();
  }

  public channelInfo() {
    return this._http.get('/api/youtube/channelInfo')
      .toPromise();
  }

}
