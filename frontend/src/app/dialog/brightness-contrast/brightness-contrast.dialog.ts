import { Component, Inject, OnInit } from '@angular/core';
import { MatLegacyDialogRef as MatDialogRef, MAT_LEGACY_DIALOG_DATA as MAT_DIALOG_DATA } from '@angular/material/legacy-dialog';
import { MatLegacySnackBar as MatSnackBar } from '@angular/material/legacy-snack-bar';
import { RcpService } from '../../service/rcp.service';
import { PtzDahuaSession } from '../../class/ptz-dahua-session';
import { DahuaParamRequestSetConfigVideoInMode } from 'src/app/class/dahua-param-request-setConfig-VideoInMode';
import { DahuaParamRequestSetConfigVideoInWhiteBalance } from 'src/app/class/dahua-param-request-setConfig-VideoInWhiteBalance';
import { DahuaParamRequestSetConfigVideoColorTable } from 'src/app/class/dahua-param-request-setConfig-VideoColorTable';

@Component({
  selector: 'app-brightness-contrast',
  templateUrl: './brightness-contrast.dialog.html',
  styleUrls: ['./brightness-contrast.dialog.css']
})
export class BrightnessContrastDialog implements OnInit {

  configStr: string = "0";

  wb = {
    "mode": "Auto",
    "red": {
      "min": 0,
      "max": 100,
      "value": 13,
      "step": 1
    },
    "blue": {
      "min": 0,
      "max": 100,
      "value": 56,
      "step": 1
    },
    "green": {
      "min": 0,
      "max": 100,
      "value": 56,
      "step": 1
    }
  }

  imagem = {
    "brilho": {
      "min": 0,
      "max": 100,
      "value": 50,
      "step": 1
    },
    "contraste": {
      "min": 0,
      "max": 100,
      "value": 50,
      "step": 1
    },
    "saturacao": {
      "min": 0,
      "max": 100,
      "value": 50,
      "step": 1
    }
  };  

  PTZVideoInMode: DahuaParamRequestSetConfigVideoInMode = new DahuaParamRequestSetConfigVideoInMode()

  PTZVideoInWhiteBalanceTable: DahuaParamRequestSetConfigVideoInWhiteBalance[] = [
    new DahuaParamRequestSetConfigVideoInWhiteBalance(),
    new DahuaParamRequestSetConfigVideoInWhiteBalance(),
    new DahuaParamRequestSetConfigVideoInWhiteBalance()
  ];

  PTZVideoColorTable: DahuaParamRequestSetConfigVideoColorTable[] = [
    new DahuaParamRequestSetConfigVideoColorTable(),
    new DahuaParamRequestSetConfigVideoColorTable(),
    new DahuaParamRequestSetConfigVideoColorTable()
  ];

  constructor(
    public dialogRef: MatDialogRef<BrightnessContrastDialog>,
    private rcp: RcpService,
    private snackBar: MatSnackBar,
    @Inject(MAT_DIALOG_DATA) public ptz: string) {
      this.loadConf();
    }

  showMsg(msg: string) {
    this.snackBar.open(msg, "Fechar", {
      duration: 3000,
    });
  }

  ngOnInit() {
  }

  private _getVideoInModeConfig(): number {
    return this.PTZVideoInMode.Config[0];
  }

  private _setVideoInModeConfig(config: number) {
    this.PTZVideoInMode.Config[0] = config;
  }

  loadConf() {
    console.log(`loadConf [${this.ptz}]`);
    (this.rcp.getSession(this.ptz) as PtzDahuaSession)
      .getConfig([ "VideoInMode", "VideoInWhiteBalance", "VideoColor" ])
      .then((result) => {

        console.log("loadConf [processando]");

        let VideoInMode = result.params[0];
        let VideoInWhiteBalance = result.params[1];
        let VideoColor = result.params[2];

        this.PTZVideoInMode = VideoInMode.params.table[0];
        this.PTZVideoInWhiteBalanceTable = VideoInWhiteBalance.params.table[0];
        this.PTZVideoColorTable = VideoColor.params.table[0];

        this.configStr = this._getVideoInModeConfig() + "";

        console.log("loadConf [config]", this.configStr);
        console.log("loadConf [PTZVideoInWhiteBalanceTable]", this.PTZVideoInWhiteBalanceTable);
        console.log("loadConf [PTZVideoColorTable]", this.PTZVideoColorTable);

        this.loadWB();
        this.loadImagem();
      });
  }

  loadWB() {
    this.wb.blue.value = this.PTZVideoInWhiteBalanceTable[this._getVideoInModeConfig()]["GainBlue"];
    this.wb.green.value = this.PTZVideoInWhiteBalanceTable[this._getVideoInModeConfig()]["GainGreen"];
    this.wb.red.value = this.PTZVideoInWhiteBalanceTable[this._getVideoInModeConfig()]["GainRed"];
    this.wb.mode = this.PTZVideoInWhiteBalanceTable[this._getVideoInModeConfig()]["Mode"];
  }

  loadImagem() {
    this.imagem.brilho.value = this.PTZVideoColorTable[this._getVideoInModeConfig()]["Brightness"];
    this.imagem.contraste.value = this.PTZVideoColorTable[this._getVideoInModeConfig()]["Contrast"];
    this.imagem.saturacao.value = this.PTZVideoColorTable[this._getVideoInModeConfig()]["Saturation"];
  }
  
  changeConf() {
    this._setVideoInModeConfig(parseInt(this.configStr));
    console.log("changeConf [config]", this._getVideoInModeConfig());
    console.log(`changeConf [${this.ptz}] [${this._getVideoInModeConfig()}]`);
    (this.rcp.getSession(this.ptz) as PtzDahuaSession)
      .setVideoInMode(this.PTZVideoInMode)
      .then(() => {
        this.loadWB();
        this.loadImagem();
        this.showMsg("VideoInMode (Ficheiro de Conf.) salvo temporariamente");
      });
  }

  changeWB() {
    console.log(`changeWB [${this.ptz}]`);
    this.PTZVideoInWhiteBalanceTable[this._getVideoInModeConfig()]["GainBlue"] = this.wb.blue.value;
    this.PTZVideoInWhiteBalanceTable[this._getVideoInModeConfig()]["GainGreen"] = this.wb.green.value;
    this.PTZVideoInWhiteBalanceTable[this._getVideoInModeConfig()]["GainRed"] = this.wb.red.value;
    this.PTZVideoInWhiteBalanceTable[this._getVideoInModeConfig()]["Mode"] = this.wb.mode;

    (this.rcp.getSession(this.ptz) as PtzDahuaSession)
      .setVideoInWhiteBalance(this.PTZVideoInWhiteBalanceTable)
      .then(() => {
        this.showMsg("VideoInWhiteBalance (WB) salvo temporariamente");
      });    
  }

  changeImagem() {
    console.log(`changeImagem [${this.ptz}]`);
    this.PTZVideoColorTable[this._getVideoInModeConfig()]["Brightness"] = this.imagem.brilho.value;
    this.PTZVideoColorTable[this._getVideoInModeConfig()]["Contrast"] = this.imagem.contraste.value;
    this.PTZVideoColorTable[this._getVideoInModeConfig()]["Saturation"] = this.imagem.saturacao.value;

    (this.rcp.getSession(this.ptz) as PtzDahuaSession)
      .setVideoColor(this.PTZVideoColorTable)
      .then(() => {
        this.showMsg("VideoColor (imagem) salvo temporariamente");
      });    
  }

  salvar() {

    (this.rcp.getSession(this.ptz) as PtzDahuaSession)
      .setConfig([ "VideoInMode", "VideoInWhiteBalance", "VideoColor" ], [
        this.PTZVideoInMode,
        this.PTZVideoInWhiteBalanceTable, 
        this.PTZVideoColorTable
      ])
      .then(() => {
        this.dialogRef.close();
      });    

  }

}
