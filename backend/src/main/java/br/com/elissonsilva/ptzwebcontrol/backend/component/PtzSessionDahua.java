package br.com.elissonsilva.ptzwebcontrol.backend.component;

import br.com.elissonsilva.ptzwebcontrol.backend.entity.DahuaSessionData;

public class PtzSessionDahua extends PtzSessionAbstract {

    private int _keepAliveInterval = 9000;
    private DahuaSessionData _sessionData = new DahuaSessionData();

    public PtzSessionDahua(String ptz, String user, String pass) {
        super(ptz, user, pass);
    }

    protected void _post( String page, String body ) {
        //if(body.method == "ptz.start") this._lastCallBody = body;
        super._post(page, body);
    }

    /////// PRIVATE METHODS /////////////////////

    private int _getSeq() {
        /*
        let b = 0;
        let a = this._sessionData.session.toString().replace(/[^0-9]/gi, "")
        , c = parseInt(a).toString(2).slice(-24)
                , d = ("00000000" + b.toString(2)).slice(-8);
        return b = (b + 1) % 256,
        parseInt(c + d, 2)

         */
        return 0;
    }

    private String _getHashPassword() {
        String pass = this._hex_md5(
                this._user + ":" +
                        this._sessionData.getRandom() + ":" +
                        this._hex_md5(this._user + ":" +
                                this._sessionData.getRealm() + ":" +
                                this._pass)).toUpperCase();
        return pass;
    }

    private String _hex_md5(String text) {
        return ""; //faultylabs.MD5(text);
    }

    private void _startSession() {
        /*
        clearTimeout(this._sessionData.timer);

        let body = {
                "method": "global.login",
                "params": {
            "userName": this._user,
                    "password": "",
                    "clientType": "Web3.0",
                    "loginType": "Direct"
        },
        "id": 4
      };
        //
        this._addLog(this._ptz, "getSession: " + JSON.stringify(body));
        return this._post( "RPC2_Login", body).then( r => {
                this._addLog(this._ptz, "getSession return: " + JSON.stringify(r));
        this._sessionData.random = r.params.random;
        this._sessionData.realm = r.params.realm;
        this._sessionData.session = r.session;
        this._sessionData.isConnected = true;
        //
        this._keepAliveTimeout();
        //
      }).catch( (error) => {
                this._sessionData.isConnected = false;
        throw error;
      } );
      */
    }

    private void _login() {
        /*
        let hashPass = this._getHashPassword();
        let body = {
                "method": "global.login",
                "params": {
            "userName": this._user,
                    "password": hashPass,
                    "clientType": "Web3.0",
                    "loginType": "Direct",
                    "authorityType": "Default"
        },
        "id":3,
                "session": this._sessionData.session
      };
        //
        this._addLog(this._ptz, "login: " + JSON.stringify(body));
        return this._post("RPC2_Login", body).then( r => {
                // TODO
                // Validate login return (check result value)
                // ERROR: {"error":{"code":268632085,"message":"Component error: User or password not valid!"},"id":3,"params":{"remainLockSecond":0,"remainLoginTimes":2},"result":false,"session":2147483643}
                // SUCCESS: {"id":3,"params":{"keepAliveInterval":60},"result":true,"session":"4f8fc4ebe6ab2b4b6a04909b3fb91686"}
                this._addLog(this._ptz, "login return: " + JSON.stringify(r));
        this._sessionData.session = r.session;
        this._sessionData.id = r.id;
      });

         */
    }

    private void _factoryInstance() {
        /*
        var body = {
                "method":"ptz.factory.instance",
                "params":{
            "channel":0
        },
        "id": this._sessionData.id + 1,
                "session": this._sessionData.session
      };
        //
        this._addLog(this._ptz, "factoryInstance : " + JSON.stringify(body));
        return this._post("RPC2", body).then( r => {
                this._addLog(this._ptz, "factoryInstance return: " + JSON.stringify(r));
        this._sessionData.result = r.result;
        this._sessionData.id = r.id;
      });

         */
    }

    private void _keepAlive() {
        /*
        if( !this.isConnected() )
            return this._getPromiseRejectWithText(`_keepAlive: ${this._ptz} is not connected`);

        var body = {
                "method":"global.keepAlive",
                "params":{
            "timeout":10000,
                    "active": true
        },
        "id": this._sessionData.id + 1,
                "session": this._sessionData.session
      };
        //
        this._addLog(this._ptz, "keepAlive : " + JSON.stringify(body));
        return this._post("RPC2", body).then( r => {
                this._addLog(this._ptz, "keepAlive return: " + JSON.stringify(r));
        this._sessionData.id = r.id;
      });

         */
    }

    private void _keepAliveTimeout() {
        /*
        this._sessionData.timer = setTimeout(() => {
                this._keepAlive().then( () => { this._keepAliveTimeout(); })
      }, this._keepAliveInterval);

         */
    }

    private void _ptzStart(String code, String arg1, String arg2, String arg3, String arg4, String channel) {
        /*
        if( !this.isConnected() )
            return this._getPromiseRejectWithText(`_ptzStart: ${this._ptz} is not connected`);

        var body = {
                "method": "ptz.start",
                "params": {
            "code": code,
                    "arg1": arg1,
                    "arg2": arg2,
                    "arg3": arg3,
                    "arg4": arg4
        },
        "id": this._sessionData.id + 1,
                "session": this._sessionData.session,
                "object": this._sessionData.result,
                "seq": this._getSeq()
      };
        if( body.params.arg1 == null ) delete body.params.arg1;
        if( body.params.arg2 == null ) delete body.params.arg2;
        if( body.params.arg3 == null ) delete body.params.arg3;
        if( body.params.arg4 == null ) delete body.params.arg4;
        //
        this._addLog(this._ptz, "_ptzStart : " + JSON.stringify(body));
        return this._post("RPC2", body).then( r => {
                this._addLog(this._ptz, "_ptzStart return: " + JSON.stringify(r));
        this._sessionData.id = r.id;
      });

         */
    }

    private void _ptzStop(String code, String arg1, String arg2, String arg3, String arg4, String channel) {
        /*
        if( !this.isConnected() )
            return this._getPromiseRejectWithText(`_ptzStop: ${this._ptz} is not connected`);

        var body = {
                "method": "ptz.stop",
                "params": {
            "code": code,
                    "arg1": arg1,
                    "arg2": arg2,
                    "arg3": arg3,
                    "arg4": arg4
        },
        "id": this._sessionData.id + 1,
                "session": this._sessionData.session,
                "object": this._sessionData.result,
                "seq": this._getSeq()
      };
        if( body.params.arg1 == null ) delete body.params.arg1;
        if( body.params.arg2 == null ) delete body.params.arg2;
        if( body.params.arg3 == null ) delete body.params.arg3;
        if( body.params.arg4 == null ) delete body.params.arg4;
        //
        this._addLog(this._ptz, "_ptzStop : " + JSON.stringify(body));
        return this._post("RPC2", body).then( r => {
                this._addLog(this._ptz, "_ptzStop return: " + JSON.stringify(r));
        this._sessionData.id = r.id;
      });

         */
    }

    /////// PUBLIC METHODS ///////////

    public boolean isConnected() {
        return this._sessionData.isConnected();
    }

    public void connect() {
        /*
        return this._startSession()
                .then( () => { return this._login(); })
        .then( () => { return this._factoryInstance(); })

         */
    }

    public void loadPreset(int id) {
        /*
        if( !this.isConnected() )
            return this._getPromiseRejectWithText(`loadPreset: ${this._ptz} is not connected`);

        let freezeFocus = false;

        return this._ptzStart("GotoPreset", id, 0, 0).then( () => {
        if(freezeFocus)
            setTimeout( () => {
                    this.startFocusOut().then(() => {
                            this.stopFocusOut() }) }, 5000);
      });

         */
    }

    public void savePreset(int id) {
        this._ptzStart("SetPreset", String.valueOf(id), String.valueOf(0), String.valueOf(0), null, null);
    }

    public void setZoomSpeed(int value) {
        /*
        //
        return this.setConfig([ "VideoInZoom" ], [ [
        {
            "DigitalZoom": false,
                "Speed": value,
                "ZoomLimit": 4
        },
        {
            "DigitalZoom": false,
                "Speed": value,
                "ZoomLimit": 4
        },
        {
            "DigitalZoom": false,
                "Speed": value,
                "ZoomLimit": 4
        }
          ] ]);
        //
        */
    }

    public void startZoomIn() {
        /*
        let amount = 5;
        return this._ptzStart("ZoomTele", amount, 0, 0);

         */
    }

    public void stopZoomIn() {
        /*
        let amount = 5;
        return this._ptzStop("ZoomTele", amount, 0, 0);

         */
    }

    public void startZoomOut() {
        /*
        let amount = 5;
        return this._ptzStart("ZoomWide", amount, 0, 0);

         */
    }

    public void stopZoomOut() {
        /*
        let amount = 5;
        return this._ptzStop("ZoomWide", amount, 0, 0);

         */
    }

    public void startFocusIn() {
        /*
        let amount = 5;
        return this._ptzStart("FocusNear", amount, 0, 0);

         */
    }

    public void stopFocusIn() {
        /*
        let amount = 5;
        return this._ptzStop("FocusNear", amount, 0, 0);

         */
    }

    public void startFocusOut() {
        /*
        let amount = 5;
        return this._ptzStart("FocusFar", amount, 0, 0);

         */
    }

    public void stopFocusOut() {
        /*
        let amount = 5;
        return this._ptzStop("FocusFar", amount, 0, 0);

         */
    }

    public void startJoystick(String direction, int speed1, int speed2) {
        /*
        return this._ptzStart(direction, speed1, speed2, 0);

         */
    }

    public void stopJoystick(String direction, int speed1, int speed2) {
        /*
        return this._ptzStop(direction, speed1, speed2, 0);

         */
    }

    public void stopLastCall() {
        /*
        if( !this.isConnected() )
            return this._getPromiseRejectWithText(`stopLastCall: ${this._ptz} is not connected`);

        if(this._lastCallBody && this._lastCallBody.method)
        {
            if(this._lastCallBody.method == "ptz.start")
            {
                this._lastCallBody.method = "ptz.stop";
                this._lastCallBody.id = this._sessionData.id + 1,
                        this._lastCallBody.session = this._sessionData.session,
                        this._lastCallBody.object = this._sessionData.result,
                        this._lastCallBody.seq = this._getSeq()
                //
                this._addLog(this._ptz, "stopLastCall : " + JSON.stringify(this._lastCallBody));
                return this._post("RPC2", this._lastCallBody).then( r => {
                        this._addLog(this._ptz, "stopLastCall return: " + JSON.stringify(r));
                this._sessionData.id = r.id;
          });
            }
        }

        return Promise.resolve();

         */
    }


    ///////////// UNDER DEVELOPMENT //////////////

    public void specificPosition(int horizontal, int vertical, int zoom) {
        /*
        //let degree = -18.99666 + 6.9773 * Math.log(horizontal);
        return this._ptzStart("PositionABS", horizontal, vertical, zoom);

         */
    }

    public void getConfig() {
        // list: any[]
        /*
        if( !this.isConnected() )
            return this._getPromiseRejectWithText(`getConfig: ${this._ptz} is not connected`);

        var body = {
                "method": "system.multicall",
                "params": [ ],
        "id": 0,
                "session": this._sessionData.session
      };

        body.params = list.map( (name, i) => { return {
                "method": "configManager.getConfig",
                "params": {
            "name": name
        },
            "id": this._sessionData.id + 1 + i,
                    "session": this._sessionData.session
        }
        });

        body.id = this._sessionData.id + 1 + list.length;

        //
        this._addLog(this._ptz, "getConfig : " + JSON.stringify(body));
        return this._post("RPC2", body).then( r => {
                this._addLog(this._ptz, "getConfig return: " + JSON.stringify(r));
        this._sessionData.id = r.id;
        return r;
      });

         */
    }

    public void setConfig() {
        // list: any[], table: any[]
        /*
        if( !this.isConnected() )
            return this._getPromiseRejectWithText(`setConfig: ${this._ptz} is not connected`);

        var body: any = {};

        if(list.length == 1) {

            body = {
                    "method": "configManager.setConfig",
                    "params": {
                "name": list[0],
                        "table": [
                table[0]
            ],
                "options": []
            },
            "id": this._sessionData.id + 1,
                    "session": this._sessionData.session
        };

        }
        else {
            body = {
                    "method": "system.multicall",
                    "params": [ ],
            "id": 0,
                    "session": this._sessionData.session
        };

            body.params = list.map( (name, i) => { return {
                    "method": "configManager.setConfig",
                    "params": {
                "name": name,
                        "table": [ table[i] ],
                "options": []
            },
                "id": this._sessionData.id + 1 + i,
                        "session": this._sessionData.session
          }
            });

            body.id = this._sessionData.id + 1 + list.length;
        }

        //
        this._addLog(this._ptz, "setConfig : " + JSON.stringify(body));
        return this._post("RPC2", body).then( r => {
                this._addLog(this._ptz, "setConfig return: " + JSON.stringify(r));
        this._sessionData.id = r.id;
        return r;
      });

         */
    }

    public void setVideoColor() {
        // videoColorTable: any[]
        /*
        if( !this.isConnected() )
            return this._getPromiseRejectWithText(`setVideoColor: ${this._ptz} is not connected`);

        var body = {
                "method": "configManager.setTemporaryConfig",
                "params": {
            "name": "VideoColor",
                    "table": videoColorTable,
                    "options": []
        },
        "id": this._sessionData.id + 1,
                "session": this._sessionData.session
      };
        //
        this._addLog(this._ptz, "setVideoColor : " + JSON.stringify(body));
        return this._post("RPC2", body).then( r => {
                this._addLog(this._ptz, "setVideoColor return: " + JSON.stringify(r));
        this._sessionData.id = r.id;
      });

         */
    }

    public void setVideoInMode(int config) {
        /*
        if( !this.isConnected() )
            return this._getPromiseRejectWithText(`setVideoInMode: ${this._ptz} is not connected`);

        var body = {
                "method": "configManager.setTemporaryConfig",
                "params": {
            "name": "VideoInMode",
                    "table": [
            {
                "Config": [
                config
              ],
                "Mode": 0,
                    "TimeSection": [
                [
                "0 00:00:00-24:00:00",
                        "0 00:00:00-24:00:00",
                        "0 00:00:00-24:00:00",
                        "0 00:00:00-24:00:00",
                        "0 00:00:00-24:00:00",
                        "0 00:00:00-24:00:00"
                ],
                [
                "0 00:00:00-24:00:00",
                        "0 00:00:00-24:00:00",
                        "0 00:00:00-24:00:00",
                        "0 00:00:00-24:00:00",
                        "0 00:00:00-24:00:00",
                        "0 00:00:00-24:00:00"
                ],
                [
                "0 00:00:00-24:00:00",
                        "0 00:00:00-24:00:00",
                        "0 00:00:00-24:00:00",
                        "0 00:00:00-24:00:00",
                        "0 00:00:00-24:00:00",
                        "0 00:00:00-24:00:00"
                ],
                [
                "0 00:00:00-24:00:00",
                        "0 00:00:00-24:00:00",
                        "0 00:00:00-24:00:00",
                        "0 00:00:00-24:00:00",
                        "0 00:00:00-24:00:00",
                        "0 00:00:00-24:00:00"
                ],
                [
                "0 00:00:00-24:00:00",
                        "0 00:00:00-24:00:00",
                        "0 00:00:00-24:00:00",
                        "0 00:00:00-24:00:00",
                        "0 00:00:00-24:00:00",
                        "0 00:00:00-24:00:00"
                ],
                [
                "0 00:00:00-24:00:00",
                        "0 00:00:00-24:00:00",
                        "0 00:00:00-24:00:00",
                        "0 00:00:00-24:00:00",
                        "0 00:00:00-24:00:00",
                        "0 00:00:00-24:00:00"
                ],
                [
                "0 00:00:00-24:00:00",
                        "0 00:00:00-24:00:00",
                        "0 00:00:00-24:00:00",
                        "0 00:00:00-24:00:00",
                        "0 00:00:00-24:00:00",
                        "0 00:00:00-24:00:00"
                ]
              ]
            }
          ],
            "options": []
        },
        "id": this._sessionData.id + 1,
                "session": this._sessionData.session
      };
        //
        this._addLog(this._ptz, "setVideoInMode : " + JSON.stringify(body));
        return this._post("RPC2", body).then( r => {
                this._addLog(this._ptz, "setVideoInMode return: " + JSON.stringify(r));
        this._sessionData.id = r.id;
      });

         */
    }

    public void setVideoInWhiteBalance() {
        // videoInWhiteBalanceTable: any[]
        /*
        if( !this.isConnected() )
            return this._getPromiseRejectWithText(`setVideoInWhiteBalance: ${this._ptz} is not connected`);

        var body = {
                "method": "configManager.setTemporaryConfig",
                "params": {
            "name": "VideoInWhiteBalance",
                    "table": [
            videoInWhiteBalanceTable
          ],
            "options": []
        },
        "id": this._sessionData.id + 1,
                "session": this._sessionData.session
      };
        //
        this._addLog(this._ptz, "setVideoInWhiteBalance : " + JSON.stringify(body));
        return this._post("RPC2", body).then( r => {
                this._addLog(this._ptz, "setVideoInWhiteBalance return: " + JSON.stringify(r));
        this._sessionData.id = r.id;
      });

         */
    }

    public void moveDirectly(int[] coord, int speed) {
        /*
        if( !this.isConnected() )
            return this._getPromiseRejectWithText(`moveDirectly: ${this._ptz} is not connected`);

        var body = {
                "method": "ptz.moveDirectly",
                "params": {
            "screen": coord,
                    "speed": speed
        },
        "id": this._sessionData.id + 1,
                "session": this._sessionData.session,
                "object": this._sessionData.result
      };
        //
        this._addLog(this._ptz, "moveDirectly : " + JSON.stringify(body));
        return this._post("RPC2", body).then( r => {
                this._addLog(this._ptz, "moveDirectly return: " + JSON.stringify(r));
        this._sessionData.session = r.session;
        this._sessionData.id = r.id;
      });

         */
    }

}
