import {Component, Inject, OnInit} from '@angular/core';
import {MatDialogRef, MAT_DIALOG_DATA} from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { RcpService } from '../../service/rcp.service';
import { PtzDahuaSession } from '../../class/ptz-dahua-session';

@Component({
  selector: 'app-brightness-contrast',
  templateUrl: './brightness-contrast.dialog.html',
  styleUrls: ['./brightness-contrast.dialog.css']
})
export class BrightnessContrastDialog implements OnInit {

  configStr: string = "0";

  config: number = 0;

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

  PTZVideoInWhiteBalanceTable: any[] = [
    {
      "ColorTemperatureLevel": 50,
      "GainBlue": 50,
      "GainGreen": 50,
      "GainRed": 50,
      "Mode": "Auto"
    },
    {
      "ColorTemperatureLevel": 50,
      "GainBlue": 50,
      "GainGreen": 50,
      "GainRed": 50,
      "Mode": "Auto"
    },
    {
      "ColorTemperatureLevel": 50,
      "GainBlue": 50,
      "GainGreen": 50,
      "GainRed": 50,
      "Mode": "Auto"
    }
  ];

  PTZVideoColorTable: any[] = [{
      "Brightness": 50,
      "ChromaSuppress": 50,
      "Contrast": 50,
      "Gamma": 50,
      "Hue": 50,
      "Saturation": 50,
      "Style": "Standard",
      "TimeSection": "0 00:00:00-24:00:00"
    },
    {
      "Brightness": 50,
      "ChromaSuppress": 50,
      "Contrast": 50,
      "Gamma": 50,
      "Hue": 50,
      "Saturation": 50,
      "Style": "Standard",
      "TimeSection": "0 00:00:00-24:00:00"
    },
    {
      "Brightness": 50,
      "ChromaSuppress": 50,
      "Contrast": 50,
      "Gamma": 50,
      "Hue": 50,
      "Saturation": 50,
      "Style": "Standard",
      "TimeSection": "1 00:00:00-24:00:00"
    }
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

  loadConf() {
    console.log(`loadConf [${this.ptz}]`);
    (this.rcp.getSession(this.ptz) as PtzDahuaSession)
      .getConfig([ "VideoInMode", "VideoInWhiteBalance", "VideoColor" ])
      .then((result) => {

        console.log("loadConf [processando]");

        let VideoInMode = result.params[0];
        let VideoInWhiteBalance = result.params[1];
        let VideoColor = result.params[2];

        this.config = VideoInMode.params.table[0].Config[0];
        this.PTZVideoInWhiteBalanceTable = VideoInWhiteBalance.params.table[0];
        this.PTZVideoColorTable = VideoColor.params.table[0];

        this.configStr = this.config + "";

        console.log("loadConf [config]", this.config);
        console.log("loadConf [PTZVideoInWhiteBalanceTable]", this.PTZVideoInWhiteBalanceTable);
        console.log("loadConf [PTZVideoColorTable]", this.PTZVideoColorTable);

        this.loadWB();
        this.loadImagem();
      });
  }

  loadWB() {
    this.wb.blue.value = this.PTZVideoInWhiteBalanceTable[this.config]["GainBlue"];
    this.wb.green.value = this.PTZVideoInWhiteBalanceTable[this.config]["GainGreen"];
    this.wb.red.value = this.PTZVideoInWhiteBalanceTable[this.config]["GainRed"];
    this.wb.mode = this.PTZVideoInWhiteBalanceTable[this.config]["Mode"];
  }

  loadImagem() {
    this.imagem.brilho.value = this.PTZVideoColorTable[this.config]["Brightness"];
    this.imagem.contraste.value = this.PTZVideoColorTable[this.config]["Contrast"];
    this.imagem.saturacao.value = this.PTZVideoColorTable[this.config]["Saturation"];
  }
  
  changeConf() {
    this.config = parseInt(this.configStr);
    console.log("changeConf [config]", this.config);
    console.log(`changeConf [${this.ptz}] [${this.config}]`);
    (this.rcp.getSession(this.ptz) as PtzDahuaSession)
      .setVideoInMode(this.config)
      .then(() => {
        this.loadWB();
        this.loadImagem();
        this.showMsg("VideoInMode (Ficheiro de Conf.) salvo temporariamente");
      });
  }

  changeWB() {
    console.log(`changeWB [${this.ptz}]`);
    this.PTZVideoInWhiteBalanceTable[this.config]["GainBlue"] = this.wb.blue.value;
    this.PTZVideoInWhiteBalanceTable[this.config]["GainGreen"] = this.wb.green.value;
    this.PTZVideoInWhiteBalanceTable[this.config]["GainRed"] = this.wb.red.value;
    this.PTZVideoInWhiteBalanceTable[this.config]["Mode"] = this.wb.mode;

    (this.rcp.getSession(this.ptz) as PtzDahuaSession)
      .setVideoInWhiteBalance(this.PTZVideoInWhiteBalanceTable)
      .then(() => {
        this.showMsg("VideoInWhiteBalance (WB) salvo temporariamente");
      });    
  }

  changeImagem() {
    console.log(`changeImagem [${this.ptz}]`);
    this.PTZVideoColorTable[this.config]["Brightness"] = this.imagem.brilho.value;
    this.PTZVideoColorTable[this.config]["Contrast"] = this.imagem.contraste.value;
    this.PTZVideoColorTable[this.config]["Saturation"] = this.imagem.saturacao.value;

    (this.rcp.getSession(this.ptz) as PtzDahuaSession)
      .setVideoColor(this.PTZVideoColorTable)
      .then(() => {
        this.showMsg("VideoColor (imagem) salvo temporariamente");
      });    
  }

  salvar() {

    this.rcp.getSession(this.ptz)
      .setConfig([ "VideoInMode" ], [
        {
          "Config": [ this.config ],
          "Mode": 0,
          "TimeSection": [
            ["0 00:00:00-24:00:00", "0 00:00:00-23:59:59", "0 00:00:00-24:00:00", "0 00:00:00-24:00:00", "0 00:00:00-24:00:00", "0 00:00:00-24:00:00" ],
            ["0 00:00:00-24:00:00", "0 00:00:00-24:00:00", "0 00:00:00-24:00:00", "0 00:00:00-24:00:00", "0 00:00:00-24:00:00", "0 00:00:00-24:00:00"],
            ["0 00:00:00-24:00:00", "0 00:00:00-24:00:00", "0 00:00:00-24:00:00", "0 00:00:00-24:00:00", "0 00:00:00-24:00:00", "0 00:00:00-24:00:00"],
            ["0 00:00:00-24:00:00", "0 00:00:00-24:00:00", "0 00:00:00-24:00:00", "0 00:00:00-24:00:00", "0 00:00:00-24:00:00", "0 00:00:00-24:00:00"],
            ["0 00:00:00-24:00:00", "0 00:00:00-24:00:00", "0 00:00:00-24:00:00", "0 00:00:00-24:00:00", "0 00:00:00-24:00:00", "0 00:00:00-24:00:00"],
            ["0 00:00:00-24:00:00", "0 00:00:00-24:00:00", "0 00:00:00-24:00:00", "0 00:00:00-24:00:00", "0 00:00:00-24:00:00", "0 00:00:00-24:00:00"],
            ["0 00:00:00-24:00:00", "0 00:00:00-24:00:00", "0 00:00:00-24:00:00", "0 00:00:00-24:00:00", "0 00:00:00-24:00:00", "0 00:00:00-24:00:00"]
          ]
        }
      ])
      .then(() => {

        return this.rcp.getSession(this.ptz)
          .setConfig([ "VideoInWhiteBalance", "VideoColor" ], [ this.PTZVideoInWhiteBalanceTable, this.PTZVideoColorTable ]);

      })
      .then(() => {
        this.dialogRef.close();
      });    

  }

}
