import {Component, Inject, Input} from '@angular/core';
import {MatDialogRef, MAT_DIALOG_DATA} from '@angular/material/dialog';

@Component({
  selector: 'app-specific-position',
  templateUrl: './specific-position.dialog.html',
  styleUrls: ['./specific-position.dialog.css']
})
export class SpecificPositionDialog {

  sliderProps = {
    segments: 36,
    strokeWidth: 10,
    radius: 360,
    gradientColorFrom: 'red',
    gradientColorTo: 'green',
    bgCircleColor: 'blue',
    showClockFace: true,
    clockFaceColor: 'green'
  }

  start = 0;
  length = 0;

  constructor(
    public dialogRef: MatDialogRef<SpecificPositionDialog>,
    @Inject(MAT_DIALOG_DATA) public data) {
  }

  handleSliderChange(event) {
    console.log(event);
  }
  
}
