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
  channelInfo: any = {};

  constructor(private youtube: YoutubeService) { }

  ngOnInit(): void {
    this.checkYoutubeIsConnected();
  }

  checkYoutubeIsConnected(): void {
    this.youtube.isConnected()
      .then((data: any) => {
        this.youtubeIsConnected = data.connected;
        this.loading = false;

        if(this.exists && !this.youtubeIsConnected) {
          setTimeout(() => {
            this.checkYoutubeIsConnected()
          }, 1000);
        } else {
          this.youtube.channelInfo().then((data: any) => {
            if(data.data.channelsList.pageInfo.totalResults > 0)
              this.channelInfo = data.data.channelsList.items[0];
          })
        }
      });
  }

  ngOnDestroy() {
    this.exists = false;
  }

}
