import {Component, Inject} from '@angular/core';
import {MatDialogRef, MAT_DIALOG_DATA} from '@angular/material/dialog';

@Component({
  selector: 'app-brightness-contrast',
  templateUrl: './brightness-contrast.dialog.html',
  styleUrls: ['./brightness-contrast.dialog.css']
})
export class BrightnessContrastDialog {

  brightness = {
    "min": 0,
    "max": 255,
    "value": 13,
    "step": 1
  }

  contrast = {
    "min": 0,
    "max": 255,
    "value": 56,
    "step": 1
  }

  iris = {
    "min": 0,
    "max": 255,
    "value": 183,
    "step": 1
  }

  constructor(
    public dialogRef: MatDialogRef<BrightnessContrastDialog>,
    @Inject(MAT_DIALOG_DATA) public ptz: string) {}

}
