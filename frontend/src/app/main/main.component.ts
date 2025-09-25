import { trigger, transition, style, animate } from '@angular/animations';
import { AfterViewInit, Component, QueryList, ViewChild, ViewChildren } from '@angular/core';
import { MatLegacyDialog as MatDialog } from '@angular/material/legacy-dialog';
import { ShortcutInput, ShortcutEventOutput, KeyboardShortcutsComponent } from "ng-keyboard-shortcuts";

import { RcpService } from '../service/rcp.service';
import { RegionDialog } from '../dialog/region/region.dialog';
import { PresetNamesDialog } from '../dialog/preset-names/preset-names.dialog';
import { BrightnessContrastDialog } from '../dialog/brightness-contrast/brightness-contrast.dialog';
import { StepByStepDialog } from '../dialog/step-by-step/step-by-step.dialog';
import { LogDialog } from '../dialog/log/log.dialog';
import { StepByStepService } from '../service/step-by-step.service';
import { StepByStepAction } from '../class/step-by-step-action';
import { NumpadComponent } from '../component/numpad/numpad.component';
import { JoystickComponent, JoystickDirection } from '../component/joystick/joystick.component';
import { PtzDahuaSession } from '../class/ptz-dahua-session';
import { YoutubeService } from '../service/youtube.service';
import { YoutubeDialog } from '../dialog/youtube/youtube.dialog';

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
export class MainComponent implements AfterViewInit {

  objectKeys = Object.keys;

  numpadMode = {};

  presetNames = {};

  logIsDivVisible = true;

  progressBarShow = true;
  progressBatStatus = "";

  activatedShortcutPtzCode = "";
  shortcuts: ShortcutInput[] = [];

  @ViewChild(KeyboardShortcutsComponent) private keyboard: KeyboardShortcutsComponent;
  @ViewChildren('numpad') numpadComponents:QueryList<NumpadComponent>;
  @ViewChildren('joystick') joystickComponents:QueryList<JoystickComponent>;

  keyboardMovementTimeout : number = 500;

  joystickKeyboardMovementTimeoutHandler = null;
  zoomInKeyboardMovementTimeoutHandler   = null;
  zoomOutKeyboardMovementTimeoutHandler  = null;

  youtubeIsConnected: boolean = false;

  constructor(public rcp: RcpService,
    public stepByStepService: StepByStepService,
    private youtube: YoutubeService,
    private dialog: MatDialog) {

  }

  runPresetByNumpad(ptz: string, preset: number)
  {
    let numpad : NumpadComponent = this.numpadComponents.find((item, idx) => item.ptz == ptz);
    if(!numpad) return false;
    //
    return numpad.runPreset(preset - 1);
  }

  runPtzTimeline(ptz: string, preset: number)
  {
    let timelineNames = this.stepByStepService.getTimelineNames(ptz);
    if(preset <= timelineNames.length) return this.runTimeline(ptz, timelineNames[preset - 1])
    //
    return false;
    //
  }

  ngAfterViewInit(): void {
    this.checkYoutubeIsConnected();
    this.rcpLoadAppConfig();
  }

  checkYoutubeIsConnected(): void {

    this.youtube.isConnected()
      .then((data: any) => {
        this.youtubeIsConnected = (data === true);
      });

  }

  rcpLoadAppConfig(): void {

    this.rcp.loadAppConfig()
      .then(_ => {

        this.numpadMode = Object.keys(this.rcp.ptzConnection).reduce((prev, curr, idx) => {
          prev[curr] = "load";
          return prev;
        }, {});

        this.presetNames = Object.keys(this.rcp.ptzConnection).reduce((prev, curr, idx) => {
          prev[curr] = [];
          return prev;
        }, {});

      }).then(_ => {

        this.shortcuts = Object.keys(this.rcp.ptzConnection).map( (ptz, idx) => {
          return {
              key: `cmd + ${idx + 1}`,
              label: `Ativar ${ptz}`,
              description: `Ativa a ${ptz} para receber os próximos atalhos`,
              command: (output: ShortcutEventOutput) => {
                //
                if(output.event.ctrlKey && (output.event.altKey || output.event.shiftKey)) return false;
                //
                this.activateShortcutPtz(ptz);

                return false;
              },
              preventDefault: true
          };
        }).concat([1,2,3,4,5,6,7,8,9,10,11,12].map((num) => {
          let keyValue : string;
          switch(num) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9: {
              keyValue = `${num}`;
              break;
            }
            case 10: {
              keyValue = `0`;
              break;
            }
            case 11: {
              keyValue = `-`;
              break;
            }
            case 12: {
              keyValue = `=`;
              break;
            }
          }
          return {
            key: keyValue,
            label: `Carregar preset ${num}`,
            description: `Carrega o preset ${num} da camera ativa`,
            command: (output: ShortcutEventOutput) => {
              //
              if(output.event.altKey || output.event.ctrlKey || output.event.shiftKey) return false;
              //
              this.runPresetByNumpad(this.activatedShortcutPtzCode, num);
              //
              return false;
            },
            preventDefault: true
          };
        })).concat(['left', 'right', 'up', 'down'].map((direction) => {
          return {
            key: direction,
            label: `Move para '${direction}'`,
            description: `Move a camera ativa para '${direction}'`,
            command: (output: ShortcutEventOutput) => {
              //
              if(output.event.altKey || output.event.ctrlKey || output.event.shiftKey) return false;
              //
              let joystick : JoystickComponent = this.joystickComponents.find((item, idx) => item.ptz == this.activatedShortcutPtzCode);
              if(!joystick) return false;
              //
              if(this.joystickKeyboardMovementTimeoutHandler) {
                clearTimeout(this.joystickKeyboardMovementTimeoutHandler);
              }
              //
              if(direction == "left")  joystick.moveByXandYSpeed(JoystickDirection.LeftUp, 1, 0);
              if(direction == "right") joystick.moveByXandYSpeed(JoystickDirection.RightDown, 1, 0);
              if(direction == "up")    joystick.moveByXandYSpeed(JoystickDirection.LeftUp, 0, 1);
              if(direction == "down")  joystick.moveByXandYSpeed(JoystickDirection.RightDown, 0, 1);
              //
              this.joystickKeyboardMovementTimeoutHandler = setTimeout((j) => {
                j.stopAll();
              }, this.keyboardMovementTimeout, joystick);
              //
              return false;
            },
            preventDefault: true
          };
        })).concat([1, 2, 3, 4, 5, 6, 7, 8, 9].map((functionKey) => {
          return {
            key: `alt + ${functionKey}`,
            label: `Ativa o ${functionKey}º passo-a-passo`,
            description: `Ativa o ${functionKey}º Passo-a-Passo da PTZ ativa (se houver)`,
            command: (output: ShortcutEventOutput) => {
              //
              if(output.event.altKey && (output.event.ctrlKey || output.event.shiftKey)) return false;
              //
              this.runPtzTimeline(this.activatedShortcutPtzCode, functionKey);
              //
              return false;
            },
            preventDefault: true
          };
        })).concat([{
            key: `escape`,
            label: `Interrompe o passo-a-passo`,
            description: `Interrompe o Passo-a-Passo ativo no momento`,
            command: (output: ShortcutEventOutput) => {
              //
              if(output.event.altKey && (output.event.ctrlKey || output.event.shiftKey)) return false;
              //
              this.stopTimeline(this.activatedShortcutPtzCode)
              //
              return false;
            },
            preventDefault: true
          },
          {
            key: `pageup`,
            label: `Zoom In`,
            description: `Zoom In para a camera ativa no momento`,
            command: (output: ShortcutEventOutput) => {
              //
              console.log('pageup');
              //
              if(this.zoomInKeyboardMovementTimeoutHandler) {
                clearTimeout(this.zoomInKeyboardMovementTimeoutHandler);
              }
              //
              let session = this.rcp.getSession(this.activatedShortcutPtzCode)
              session.startZoomIn();
              //
              this.zoomInKeyboardMovementTimeoutHandler = setTimeout((s) => {
                s.stopZoomIn();
              }, this.keyboardMovementTimeout + 200, session);
              //
              return false;
            },
            preventDefault: true
          },
          {
            key: `pagedown`,
            label: `Zoom Out`,
            description: `Zoom Out para a camera ativa no momento`,
            command: (output: ShortcutEventOutput) => {
              //
              console.log('pagedown');
              //
              if(this.zoomOutKeyboardMovementTimeoutHandler) {
                clearTimeout(this.zoomOutKeyboardMovementTimeoutHandler);
              }
              //
              let session = this.rcp.getSession(this.activatedShortcutPtzCode)
              session.startZoomOut();
              //
              this.zoomOutKeyboardMovementTimeoutHandler = setTimeout((s) => {
                s.stopZoomOut();
              }, this.keyboardMovementTimeout + 200, session);
              //
              return false;
            },
            preventDefault: true
          }]
        ).concat(this.rcp.customShortcuts.map( s => {
          return {
            key: s.key,
            label: `Custom Key`,
            description: `Custom Key`,
            command: (output: ShortcutEventOutput) => {
              //
              console.log(`custom key ${s.key}`);
              //
              s.action.forEach(a => eval(a));
              //
              return false;
              //
            },
            preventDefault: true
          }
        }));

      }).then(_ => {

        let temp = JSON.parse(localStorage.getItem('PTZNames'));
        if(temp) this.presetNames = temp;
        else localStorage.setItem('PTZNames', JSON.stringify(this.presetNames));
        //
        Object.keys(this.rcp.ptzConnection).forEach( (p) => this.stepByStepService.initialize(p) );
        this.stepByStepService.saveTimelineAllData();
        //
        this.progressBatStatus = "CONECTANDO ..."
        return Promise.all(
          Object.keys(this.rcp.ptzConnection).map( (p) => this.rcp.getSession(p).connect()  )
        ).then( _ => {
          this.progressBarShow = false;
        }).then( _ => {
          //
          this.rcp.getPresetNames().then( (result: any) => {
            this.presetNames = result;
          })
          //
        });

      })
      .catch((err) => {
        this.progressBarShow = false;
      });

  }

  activateShortcutPtz(ptz: string) {
    this.activatedShortcutPtzCode = ptz;
  }

  isShortcutActivated(ptz: string) : boolean {
    return this.activatedShortcutPtzCode == ptz;
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

  openYoutubeDialog(): void {
    const dialogRef = this.dialog.open(YoutubeDialog, {
      width: '700px'
    });

    dialogRef.afterClosed().subscribe(result => {
      this.checkYoutubeIsConnected();
    });

  }

  setZoomSpeed(ptz, value) {
    this.rcp.getSession(ptz).setZoomSpeed(value);
  }

  startStreaming() {
    this.rcp.startStreamingCommands.forEach( action => {
      eval(action);
    });
  }

  stopStreaming() {
    this.rcp.stopStreamingCommands.forEach( action => {
      eval(action);
    });
  }

  RegionChanged(ptz) {
    let coord = [3641, 4442, 4861, 6102];
    let limit = function(me, a, b): any {
      return Math.min(b, Math.max(a, me));
    }
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

    var c = limit(((coord[0] + coord[2]) / 16384), -1, 1)
      , d = limit(((coord[1] + coord[3]) / 16384), -1, 1)
      , e = Math.abs(coord[3] - coord[1]) > Math.abs(coord[2] - coord[0]) ? coord[3] - coord[1] : coord[2] - coord[0]
      , g = limit((e / 8192), -1, 1)
      , h = coord[2] > coord[0] ? g * g : -1 * g * g;
    (this.rcp.getSession(ptz) as PtzDahuaSession).moveDirectly([c, d, h], null);
  }

}
