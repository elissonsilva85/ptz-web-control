import { Component, Inject, OnInit, ElementRef } from '@angular/core';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

import { FormControl } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { StepByStepAction } from 'src/app/class/step-by-step-action';
import { CdkDragStart, CdkDragMove, CdkDragDrop, moveItemInArray } from '@angular/cdk/drag-drop';
import { ViewChild } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { StepByStepService } from 'src/app/service/step-by-step.service';
import { PtzDahuaSession } from 'src/app/class/ptz-dahua-session';
import { ConfirmDialog } from '../confirm/confirm.dialog';


// https://stackblitz.com/edit/angular-material-drag-copy-y3p7zz?file=app%2Fapp.component.ts


@Component({
  selector: 'app-step-by-step',
  templateUrl: './step-by-step.dialog.html',
  styleUrls: ['./step-by-step.dialog.css']
})
export class StepByStepDialog implements OnInit {

  public timelineNameControl: FormControl = new FormControl();
  public waitingTimeControl: FormControl[] = [];
  public filteredOptions: Observable<any>;

  @ViewChild('availableActionsList') child: ElementRef;

  public step = -1;
  private _currentIndex;
  private _currentField;

  constructor(
    public dialogRef: MatDialogRef<StepByStepDialog>,
    @Inject(MAT_DIALOG_DATA) public data: { ptz: string, session: PtzDahuaSession },
    private snackBar: MatSnackBar,
    public stepByStepService: StepByStepService,
    private dialog: MatDialog ) {
  }

  ngOnInit() {
    this._loadDatabase();
    this._updateFilteredOptions();

    /*
    // Apply filter using the fromControl value
    this.filteredOptions = this.timelineNameControl.valueChanges
      .pipe(
        startWith(''),
        map(value => typeof value === 'string' ? value : value.name),
        map(name => name ? this._filter(name) : this.timelineNames.slice())
      );
    */

    this.dialogRef
      .beforeClosed()
      .subscribe(() => {
        this.stepByStepService.saveTimelineAllData()
      })
  }

  public displayFn(name: any): string {
    return name ? name : '';
  }

  private _filter(name: string): any[] {
    const filterValue = name.toLowerCase();
    return this.stepByStepService
      .getTimelineNames(this.data.ptz)
      .filter(option =>
        option.toLowerCase().indexOf(filterValue) === 0);
  }

  private _loadDatabase() {
    this.stepByStepService.getTimelineCurrentAction(this.data.ptz).forEach( (a,i) => {
      this.waitingTimeControl[i] = new FormControl(a.getWaitingTime());
      this.waitingTimeControl[i].valueChanges.subscribe((value) => {
        a.setWaitingTime( parseInt(value) );
        this.stepByStepService.updateActionTimes(this.data.ptz);
      });
    });
  }

  private _updateFilteredOptions() {
    this.filteredOptions = of(this.stepByStepService.getTimelineNames(this.data.ptz));
  }

  loadTimeline() {
    // Get timeline name
    let name = this.timelineNameControl.value;
    if(!name) return;

    if(this.stepByStepService.loadTimelineByNameIntoCurrentAction(this.data.ptz,name)) {
      this.stepByStepService.getTimelineCurrentAction(this.data.ptz).forEach( (a,i) => {
        this.waitingTimeControl[i] = new FormControl(a.getWaitingTime());
        this.waitingTimeControl[i].valueChanges.subscribe((value) => {
          a.setWaitingTime( parseInt(value) );
          this.stepByStepService.updateActionTimes(this.data.ptz);
        });
      });
      this.step = -1;
    }
    else
      this.snackBar.open(`Nenhum registro encontrado para '${name}'`, "Fechar", {
        duration: 3000,
      });
  }

  saveTimeline() {

    // Get timeline name
    let name = this.timelineNameControl.value;
    if(!name) {
      this.timelineNameControl.setErrors({'incorrect': true});
      this.snackBar.open("Linha do tempo precisa de um nome", "Fechar", {
        duration: 3000,
      });
      return;
    }

    this.stepByStepService.saveTimelineByName(this.data.ptz, name);

    this._updateFilteredOptions();

    // Show a message
    this.snackBar.open("Linha do tempo salva com sucesso", "Fechar", {
      duration: 2000,
    });

  }

  removeTimeline() {

    let name = this.timelineNameControl.value;
    if(!name) {
      this.timelineNameControl.setErrors({'incorrect': true});
      this.snackBar.open("Escolha uma linha do tempo que já foi salva", "Fechar", {
        duration: 3000,
      });
      return;
    }

    if(!this.stepByStepService.getTimelineNames(this.data.ptz).includes(name)) {
      this.timelineNameControl.setErrors({'incorrect': true});
      this.snackBar.open(`Não há um itme com o nome ${name} na lista de ações`, "Fechar", {
        duration: 3000,
      });
      return;
    }

    const dialogRef = this.dialog.open(ConfirmDialog, {
      data: { title: "Confirmação", message: `Você tem certeza que deseja remover o item ${name} ?` }
    });

    dialogRef.afterClosed().subscribe(result => {

      if(result !== true) return;

      this.step = -1;
      this.stepByStepService.removeTimeline(this.data.ptz, name);
      this.clearTimeline();
      this._updateFilteredOptions();

    });

  }

  clearTimeline() {
    this.stepByStepService.clearTimeline(this.data.ptz);
    this.timelineNameControl.setValue("");
    this.step = -1;
  }

  runTimeline() {
    this.step = -1;
    this.stepByStepService.runTimeline(this.data.ptz, this.data.session);
  }

  getClasses(action: StepByStepAction) {
    return {
      'step' : true,
      'step-pending': this.stepByStepService.isTimelineRunning(this.data.ptz) && action.isPending(),
      'step-running': this.stepByStepService.isTimelineRunning(this.data.ptz) && action.isRunning(),
      'step-done': this.stepByStepService.isTimelineRunning(this.data.ptz) && action.isDone(),
    }
  }

  // ================================================================================

  setStep(index: number) {
    this.step = index;
  }

  nextStep() {
    this.step++;
  }

  prevStep() {
    this.step--;
  }

  removeItem(index: number) {
    this.step = -1;
    this.stepByStepService.getTimelineCurrentAction(this.data.ptz).splice(index, 1);
    this.stepByStepService.updateActionTimes(this.data.ptz);
  }

  testItem(index: number) {
    this.stepByStepService.testItem(this.data.ptz, index, this.data.session);
  }

  // ================================================================================

  dragStart(event: CdkDragStart) {
    this._currentIndex = this.stepByStepService.availableActions.indexOf(event.source.data); // Get index of dragged type
    this._currentField = this.child.nativeElement.children[this._currentIndex]; // Store HTML field
  }

  moved(event: CdkDragMove) {
    // Check if stored HTML field is as same as current field
    if (this.child.nativeElement.children[this._currentIndex] !== this._currentField) {
      // Replace current field, basically replaces placeholder with old HTML content
      this.child.nativeElement.replaceChild(this._currentField, this.child.nativeElement.children[this._currentIndex]);
    }
  }

  itemDropped(event: CdkDragDrop<any[]>) {
    if (event.previousContainer === event.container) {
      moveItemInArray(this.stepByStepService.getTimelineCurrentAction(this.data.ptz), event.previousIndex, event.currentIndex);
      moveItemInArray(this.waitingTimeControl, event.previousIndex, event.currentIndex);
    } else {
      this.addField(event.item.data, event.currentIndex);
    }
  }

  addField(action: StepByStepAction, index: number) {
    this.stepByStepService.getTimelineCurrentAction(this.data.ptz).splice(index, 0, action.clone() );
    this.waitingTimeControl.splice(index, 0, new FormControl('0') );
    this.waitingTimeControl[index].valueChanges.subscribe((value) => {
      this.stepByStepService.getTimelineCurrentAction(this.data.ptz)[index].setWaitingTime( parseInt(value) );
      this.stepByStepService.updateActionTimes(this.data.ptz);
    });
    this.stepByStepService.updateActionTimes(this.data.ptz);
  }

}
