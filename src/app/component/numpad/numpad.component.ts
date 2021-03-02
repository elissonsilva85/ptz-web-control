import { ChangeDetectorRef } from '@angular/core';
import { Component, OnInit, Output, EventEmitter, Input } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { RcpService } from '../../service/rcp.service';

@Component({
  selector: 'app-numpad',
  templateUrl: './numpad.component.html',
  styleUrls: ['./numpad.component.css']
})
export class NumpadComponent implements OnInit {

  @Input() ptz: string;
  @Input() mode: string = "load"; // load | save
  @Input() labels: string[] = [];

  __labelsDefault: string[] = [];

  labelsDisabled: boolean[] = [];

  constructor(private rcp: RcpService, 
    private snackBar: MatSnackBar,
    private ref: ChangeDetectorRef) { }

  ngOnInit(): void {

    for(let i=0; i<12; i++)
    {
      this.__labelsDefault.push("-");
      this.labelsDisabled.push(false);
    }

    this.labels = this.__labelsDefault.map( 
      (e,i) => { 
        return ( this.labels[i] ? this.labels[i] : e ) 
      })
  }

  getClasses(idx: number) {
    return {
      'digit' : true, 
      'disabled': this.labelsDisabled[idx],
      'saveMode': this.mode == 'save' 
    }
  }

  showError(err: string, id: number) {
    this.snackBar.open(err, "Fechar", {
      duration: 3000,
    }).afterDismissed().subscribe(info => {
      this.labelsDisabled[id] = false;
      this.ref.detectChanges();
    });
  }

  runPreset(id: number, event: MouseEvent) {
    if(!this.labelsDisabled[id])
    {
      this.labelsDisabled[id] = true;
      //
      // ---------------------------------------------------------
      if(this.mode == "load")
        this.rcp.getSession(this.ptz)
          .loadPreset(id + 1)
          .then( () => {
            this.labelsDisabled[id] = false;
          })
          .catch( (err) => {
            this.showError(err, id);
          })
      //
      // ---------------------------------------------------------
      if(this.mode == "save")
        this.rcp.getSession(this.ptz)
          .savePreset(id + 1)
          .then( () => {
            this.labelsDisabled[id] = false;
          })
          .catch( (err) => {
            this.showError(err, id);
          })
    }
  }

}
