import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { firstValueFrom } from 'rxjs/internal/firstValueFrom';

@Injectable({
  providedIn: 'root'
})
export class YoutubeService {

  constructor(private _http: HttpClient) {  }

  public isConnected(): Promise<any> {
    return firstValueFrom(this._http.get('/api/youtube/isConnected'), { defaultValue: false });
  }

  public connect() {
    window.open('/api/youtube/connect', '_blank', 'location=no,height=570,width=520,scrollbars=yes,status=yes');
  }

  public disconnect(): Promise<any> {
    return firstValueFrom(this._http.get('/api/youtube/disconnect'));
  }

  public channelInfo(): Promise<any> {
    return firstValueFrom(this._http.get('/api/youtube/channelInfo'));
  }

}
