import {Component, Inject} from '@angular/core';
import {MatLegacyDialogRef as MatDialogRef, MAT_LEGACY_DIALOG_DATA as MAT_DIALOG_DATA} from '@angular/material/legacy-dialog';


@Component({
  selector: 'app-region-dialog',
  templateUrl: './region.dialog.html',
  styleUrls: ['./region.dialog.css']
})
export class RegionDialog {

  constructor(
    public dialogRef: MatDialogRef<RegionDialog>,
    @Inject(MAT_DIALOG_DATA) public ptz: string) {}
  
  move(r, c) {
    console.log(r, c);
  }
}
