import { Component, Inject } from '@angular/core';
import { RcpService } from 'src/app/service/rcp.service';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-log-dialog',
  templateUrl: './log.dialog.html',
  styleUrls: ['./log.dialog.css']
})
export class LogDialog {

  constructor(
    public dialogRef: MatDialogRef<LogDialog>,
    @Inject(MAT_DIALOG_DATA) public rcp: RcpService) {}

  close(): void {
    this.dialogRef.close();
  }

}
