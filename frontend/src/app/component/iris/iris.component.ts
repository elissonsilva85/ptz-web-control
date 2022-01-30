import { Component, OnInit, Input } from '@angular/core';
import { RcpService } from '../../service/rcp.service';

@Component({
  selector: 'app-iris',
  templateUrl: './iris.component.html',
  styleUrls: ['./iris.component.css']
})
export class IrisComponent implements OnInit {

  @Input() ptz: string;

  constructor(public rcp: RcpService) { }

  ngOnInit(): void {
  }

}
