import { Component, OnInit, Input } from '@angular/core';
import { RcpService } from '../../service/rcp.service';

@Component({
  selector: 'app-zoom',
  templateUrl: './zoom.component.html',
  styleUrls: ['./zoom.component.css']
})
export class ZoomComponent implements OnInit {

  @Input() ptz: string;

  constructor(public rcp: RcpService) { }

  ngOnInit(): void {
  }

}
