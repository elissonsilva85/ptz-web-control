import { trigger, transition, style, animate } from '@angular/animations';
import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';

import { RcpService } from '../service/rcp.service';
import { RegionDialog } from '../dialog/region/region.dialog';
import { PresetNamesDialog } from '../dialog/preset-names/preset-names.dialog';
import { BrightnessContrastDialog } from '../dialog/brightness-contrast/brightness-contrast.dialog';
import { StepByStepDialog } from '../dialog/step-by-step/step-by-step.dialog';
import { LogDialog } from '../dialog/log/log.dialog';
import { StepByStepService } from '../service/step-by-step.service';
import { StepByStepAction } from '../class/step-by-step-action';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css'],
  animations: [
    trigger('inOutAnimation', [
      transition(':enter', [style({ opacity: 0 }), animate('0.25s ease-out', style({ opacity: 1 }))]),
      transition(':leave', [style({ opacity: 1 }), animate('0.25s ease-in', style({ opacity: 0 }))]),
    ]),
  ],
})
export class MainComponent implements OnInit {

  numpadMode = {};

  presetNames = {};

  logIsDivVisible = true;

  progressBarShow = true;
  progressBatStatus = "";

  constructor(public rcp: RcpService,
    public stepByStepService: StepByStepService,
    private dialog: MatDialog) {
    
  }

  ngOnInit(): void {
    
    
    this.rcp.loadAppConfig()
      .then(_ => {
    
        this.numpadMode = this.rcp.ptzCodes.reduce((prev, curr, idx) => {
          prev[curr] = "load";
          return prev;
        }, {});
    
        this.presetNames = this.rcp.ptzCodes.reduce((prev, curr, idx) => {
          prev[curr] = [];
          return prev;
        }, {});

      })
      .then(_ => {

        let temp = JSON.parse(localStorage.getItem('PTZNames'));
        if(temp) this.presetNames = temp;
        else localStorage.setItem('PTZNames', JSON.stringify(this.presetNames));
        //
        this.rcp.ptzCodes.forEach( p => this.stepByStepService.initialize(p) );
        this.stepByStepService.saveTimelineAllData();
        //
        this.progressBatStatus = "CONECTANDO ..."
        return Promise.all(
          this.rcp.ptzCodes.map( ptz => this.rcp.getSession(ptz).connect()  )
        ).then(_ => {
          this.progressBarShow = false;
        });

      })
      .catch((err) => {
        this.progressBarShow = false;
      });


  }

  runTimeline(ptz: string, name: string) {
    this.stepByStepService.runTimelineByName(ptz, name, this.rcp.getSession(ptz));
  }

  stopTimeline(ptz: string) {
    this.stepByStepService.stopTimeline(ptz, this.rcp.getSession(ptz))
  }

  getClasses(ptz: string, action: StepByStepAction) {
    return {
      'step' : true, 
      'step-pending': this.stepByStepService.isTimelineRunning(ptz) && action.isPending(),
      'step-running': this.stepByStepService.isTimelineRunning(ptz) && action.isRunning(),
      'step-done': this.stepByStepService.isTimelineRunning(ptz) && action.isDone(),
    }
  }

  changeNumpadMode(ptz: string) {
    this.numpadMode[ptz] = this.numpadMode[ptz] == "load" ? "save" : "load";
  }

  openBrightnessContrastDialog(ptz: string): void {
    const dialogRef = this.dialog.open(BrightnessContrastDialog, {
      width: '700px',
      data: ptz
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed', result);
    });
  }

  openRegionDialog(ptz: string): void {
    const dialogRef = this.dialog.open(RegionDialog, {
      width: '850px',
      data: ptz
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed', result);
    });
  }

  openPresetNamesDialog(ptz: string, labels: any): void {
    
    const dialogRef = this.dialog.open(PresetNamesDialog, {
      width: '690px',
      data: { "ptz": ptz, "labels": labels }
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed', result);
    });
  }

  openStepByStepDialog(ptz: string): void {

    const dialogRef = this.dialog.open(StepByStepDialog, {
      width: '100vw',
      height: '90vh',
      data: { "ptz": ptz, "session": this.rcp.getSession(ptz) }
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed', result);
    });
  }

  openLogDialog(): void {

    const dialogRef = this.dialog.open(LogDialog, {
      hasBackdrop: false,
      data: this.rcp
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed', result);
    });
  }

  limit(me, a, b): any {
    return Math.min(b, Math.max(a, me));
  }

  setZoomSpeed(ptz, value) {
    this.rcp.getSession(ptz).setZoomSpeed(value);
  }

  RegionChanged(ptz) {
    let coord = [3641, 4442, 4861, 6102];
    //
    // [ Canto superior esquerdo ]
    // a = "RegionChanged", b = (4) [1350, 1192, 376, 0]
    // Primeiro click = 1350, 1192
    // Segundo click  = 376, 0 (x,y)
    //
    // [ Canto inferior direito ]
    // a = "RegionChanged", b = (4) [6654, 7050, 8143, 8171]
    // Primeiro click = 6654, 7050
    // Segundo click  = 8143, 8171 (x,y)
    //
    //   _ 0 __
    // 0 |     |
    //   |_____| 8183 (x)
    //        8171 (y)
    //
    
    var c = this.limit(((coord[0] + coord[2]) / 16384), -1, 1)
      , d = this.limit(((coord[1] + coord[3]) / 16384), -1, 1)
      , e = Math.abs(coord[3] - coord[1]) > Math.abs(coord[2] - coord[0]) ? coord[3] - coord[1] : coord[2] - coord[0]
      , g = this.limit((e / 8192), -1, 1)
      , h = coord[2] > coord[0] ? g * g : -1 * g * g;
    this.rcp.getSession(ptz).moveDirectly([c, d, h], null);
  }

}
