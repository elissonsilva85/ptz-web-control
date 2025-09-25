import {Component, Inject} from '@angular/core';
import {MatLegacyDialogRef as MatDialogRef, MAT_LEGACY_DIALOG_DATA as MAT_DIALOG_DATA} from '@angular/material/legacy-dialog';

@Component({
  selector: 'app-preset-names',
  templateUrl: './preset-names.dialog.html',
  styleUrls: ['./preset-names.dialog.css']
})
export class PresetNamesDialog {

  options = [];

  constructor(
    public dialogRef: MatDialogRef<PresetNamesDialog>,
    @Inject(MAT_DIALOG_DATA) public data) {
    for(var label in data.labels)
      this.options.push({
        label: label,
        value: data.labels[label] == '-' ? '' : data.labels[label]
      });
  }

  salvar() {
    this.options.forEach( e => {
      this.data.labels[e.label] = e.value ? e.value : '-';
    });

    let temp = JSON.parse(localStorage.getItem('PTZNames'));
    temp[this.data.ptz] = this.data.labels;
    localStorage.setItem('PTZNames', JSON.stringify(temp));

    this.dialogRef.close();
  }
  
}
