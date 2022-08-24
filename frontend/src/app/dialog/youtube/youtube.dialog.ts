import { Component, OnDestroy, OnInit } from '@angular/core';
import { YoutubeService } from 'src/app/service/youtube.service';

@Component({
  selector: 'app-youtube',
  templateUrl: './youtube.dialog.html',
  styleUrls: ['./youtube.dialog.css']
})
export class YoutubeDialog implements OnInit, OnDestroy {

  exists: boolean = true;
  loading: boolean = true;
  youtubeIsConnected: boolean = false;
  channelInfoList: any[] = [];

  constructor(private youtube: YoutubeService) { }

  ngOnInit(): void {
    this.checkYoutubeIsConnected();
  }

  checkYoutubeIsConnected(): void {
    this.youtube.isConnected()
      .then((data: any) => {
        this.youtubeIsConnected = (data === true);
        this.loading = false;

        if(this.exists && !this.youtubeIsConnected) {
          setTimeout(() => {
            this.checkYoutubeIsConnected()
          }, 1000);
        } else {
          this.youtube.channelInfo().then((data: any[]) => {
            console.log(data);
            this.channelInfoList = data;
          })
        }
      });
  }

  ngOnDestroy() {
    this.exists = false;
  }

  connect() {
    this.youtube.connect();
  }

  disconnect() {
    this.youtube.disconnect().then(_ => this.checkYoutubeIsConnected());
  }

}
