import { Component, OnInit, Input } from '@angular/core';
import { RcpService } from '../../service/rcp.service';

@Component({
  selector: 'app-focus',
  templateUrl: './focus.component.html',
  styleUrls: ['./focus.component.css']
})
export class FocusComponent implements OnInit {

  @Input() ptz: string;

  constructor(public rcp: RcpService) { }

  ngOnInit(): void {
  }

}
