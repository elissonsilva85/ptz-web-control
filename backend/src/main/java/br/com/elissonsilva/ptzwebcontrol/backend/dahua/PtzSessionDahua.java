package br.com.elissonsilva.ptzwebcontrol.backend.dahua;

import br.com.elissonsilva.ptzwebcontrol.backend.component.PtzSessionAbstract;
import br.com.elissonsilva.ptzwebcontrol.backend.dahua.entity.*;
import br.com.elissonsilva.ptzwebcontrol.backend.dahua.entity.param.DahuaParamRequestSetConfig;
import br.com.elissonsilva.ptzwebcontrol.backend.dahua.exception.PtzSessionDahuaException;
import br.com.elissonsilva.ptzwebcontrol.backend.entity.DahuaSessionData;
import br.com.elissonsilva.ptzwebcontrol.backend.exception.PtzSessionException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.Response;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class PtzSessionDahua extends PtzSessionAbstract {

    private DahuaSessionData _sessionData;

    private DahuaRequestStart lastCall;

    public PtzSessionDahua(String ptz, String user, String pass, String url) {
        super(ptz, user, pass, url);
    }

    protected Response _post(String page, DahuaRequestBase requestBase ) throws PtzSessionException {
        if(requestBase instanceof DahuaRequestStart)
            this.lastCall = (DahuaRequestStart) requestBase;

        //SimpleBeanPropertyFilter theFilter = SimpleBeanPropertyFilter.serializeAllExcept("intValue");
        //FilterProvider filters = new SimpleFilterProvider().addFilter("filterArg", theFilter);
        //mapper.writer(filters).writeValueAsString(requestBase);

        String body = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            body = mapper.writeValueAsString(requestBase);
            return super._post(page, body);
        } catch (JsonProcessingException e) {
            throw new PtzSessionDahuaException(e);
        } catch (IOException e) {
            throw new PtzSessionDahuaException(e);
        }
    }

    /////// PRIVATE METHODS /////////////////////

    protected DahuaSessionData getSessionData() {
        return _sessionData;
    }

    protected void setSessionData(DahuaSessionData _sessionData) {
        this._sessionData = _sessionData;
    }

    protected long getSeq() {
        // pega só os numeros
        String session = this._sessionData.getSession();
        String onlyNumbers = session.replaceAll("[^0-9]", "");

        // converte pra numero seguindo o comportamento do parsetInt no Javascript
        // https://262.ecma-international.org/6.0/#sec-parseint-string-radix
        BigInteger num = null;
        if(onlyNumbers.length() > 20)
        {
            num = new BigInteger(onlyNumbers.substring(0,20));
            int rest = Integer.parseInt(onlyNumbers.substring(20));
        }
        else
        {
            num = new BigInteger(onlyNumbers);
        }

        // pega os primeiros 24 bits
        String fullBinary = num.toString(2);
        String last24bits = fullBinary.substring(fullBinary.length() - 24);

        // inclue o Zero Byte no começo e converte pra numero
        String plusZeroByte = last24bits + "00000000";
        return Long.parseLong(plusZeroByte, 2);
    }

    protected String getHashPassword() throws PtzSessionException {
        String pass = this.hexMD5(
                    this._user + ":" +
                        this._sessionData.getRandom() + ":" +
                        this.hexMD5(this._user + ":" +
                            this._sessionData.getRealm() + ":" +
                            this._pass)).toUpperCase();
        return pass;
    }

    protected String hexMD5(String text) throws PtzSessionException {

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(text.getBytes());
            byte[] digest = md.digest();
            String myHash = DatatypeConverter
                    .printHexBinary(digest).toUpperCase();

            return myHash;
        } catch (NoSuchAlgorithmException e) {
            throw new PtzSessionDahuaException(e);
        }

    }

    protected void startSession() throws PtzSessionException {

        DahuaRequestLogin requestLogin = new DahuaRequestLogin();
        requestLogin.getParams().setUserName(this._user);
        requestLogin.setId(4);

        String sessionReturn = "";
        try {
            Response response = this._post( "RPC2_Login", requestLogin);
            sessionReturn = response.body().string();
        } catch (IOException e) {
            throw new PtzSessionDahuaException(e);
        }

        DahuaResponseLogin responseLogin = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            responseLogin = mapper.readValue(sessionReturn, DahuaResponseLogin.class);
        } catch (JsonProcessingException e) {
            throw new PtzSessionDahuaException(e);
        }

        //
        // Do not need error validation
        // This request always return error, it is used to get session number
        //

        this.setSessionData(
                new DahuaSessionData(
                        responseLogin.getParams().getRandom(),
                        responseLogin.getParams().getRealm(),
                        responseLogin.getSession(),
                        true));

    }

    protected void login() throws PtzSessionException {

        String hashPass = this.getHashPassword();;

        DahuaRequestLogin requestLogin = new DahuaRequestLogin();
        requestLogin.getParams().setUserName(this._user);
        requestLogin.getParams().setPassword(hashPass);
        requestLogin.getParams().setAuthorityType("Default");
        requestLogin.setId(3);
        requestLogin.setSession(this.getSessionData().getSession());

        String sessionReturn = "";
        try {
            Response response = this._post( "RPC2_Login", requestLogin);
            sessionReturn = response.body().string();
        } catch (IOException e) {
            throw new PtzSessionDahuaException(e);
        }

        DahuaResponseLogin responseLogin = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            responseLogin = mapper.readValue(sessionReturn, DahuaResponseLogin.class);
        } catch (JsonProcessingException e) {
            throw new PtzSessionDahuaException(e);
        }

        if(responseLogin.getError() != null)
            throw new PtzSessionDahuaException(responseLogin.getError().getMessage());

        this.getSessionData()
                .updateSession(
                        responseLogin.getSession(),
                        responseLogin.getId()
                );
    }

    protected void factoryInstance() throws PtzSessionException {

        DahuaRequestFactoryInstance factoryInstance = new DahuaRequestFactoryInstance();
        factoryInstance.setId(3);
        factoryInstance.setSession(this.getSessionData().getSession());

        String sessionReturn = "";
        try {
            Response response = this._post( "RPC2", factoryInstance);
            sessionReturn = response.body().string();
        } catch (IOException e) {
            throw new PtzSessionDahuaException(e);
        }

        DahuaResponseFactoryInstance responseFactoryInstance = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            responseFactoryInstance = mapper.readValue(sessionReturn, DahuaResponseFactoryInstance.class);
        } catch (JsonProcessingException e) {
            throw new PtzSessionDahuaException(e);
        }

        if(responseFactoryInstance.getError() != null)
            throw new PtzSessionDahuaException(responseFactoryInstance.getError().getMessage());

        this.getSessionData()
                .updateSession(
                        responseFactoryInstance.getId(),
                        responseFactoryInstance.getResult()
                );

    }

    public void keepAlive() throws PtzSessionException {

        DahuaRequestKeepAlive keepAlive = new DahuaRequestKeepAlive();
        keepAlive.setId(this.getSessionData().getNextId());
        keepAlive.setSession(this.getSessionData().getSession());

        String sessionReturn = "";
        try {
            Response response = this._post( "RPC2", keepAlive);
            sessionReturn = response.body().string();
        } catch (IOException e) {
            throw new PtzSessionDahuaException(e);
        }

        DahuaResponseKeepAlive responseKeepAlive = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            responseKeepAlive = mapper.readValue(sessionReturn, DahuaResponseKeepAlive.class);
        } catch (JsonProcessingException e) {
            throw new PtzSessionDahuaException(e);
        }

        if(responseKeepAlive.getError() != null)
            throw new PtzSessionDahuaException(responseKeepAlive.getError().getMessage());


        this.getSessionData()
                .updateSession(
                        responseKeepAlive.getId()
                );
    }

    protected void ptzStart(String code, int arg1, int arg2, int arg3, int arg4, String channel) throws PtzSessionException {

        DahuaRequestStart requestStart = new DahuaRequestStart();
        requestStart.getParams().setCode(code);
        requestStart.getParams().setArg1(arg1);
        requestStart.getParams().setArg2(arg2);
        requestStart.getParams().setArg3(arg3);
        requestStart.getParams().setArg4(arg4);
        //
        requestStart.setId(this.getSessionData().getNextId());
        requestStart.setSession(this.getSessionData().getSession());
        requestStart.setObject(this.getSessionData().getResult());
        requestStart.setSeq(this.getSeq());

        String sessionReturn = "";
        try {
            Response response = this._post( "RPC2", requestStart);
            sessionReturn = response.body().string();
        } catch (IOException e) {
            throw new PtzSessionDahuaException(e);
        }

        DahuaResponseStartStop responseStart = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            responseStart = mapper.readValue(sessionReturn, DahuaResponseStartStop.class);
        } catch (JsonProcessingException e) {
            throw new PtzSessionDahuaException(e);
        }

        if(responseStart.getError() != null)
            throw new PtzSessionDahuaException(responseStart.getError().getMessage());

        this.getSessionData()
                .updateSession(
                        responseStart.getId()
                );
    }

    protected void ptzStop(String code, int arg1, int arg2, int arg3, int arg4, String channel) throws PtzSessionException {

        DahuaRequestStop requestStop = new DahuaRequestStop();
        requestStop.getParams().setCode(code);
        requestStop.getParams().setArg1(arg1);
        requestStop.getParams().setArg2(arg2);
        requestStop.getParams().setArg3(arg3);
        requestStop.getParams().setArg4(arg4);
        //
        requestStop.setId(this.getSessionData().getNextId());
        requestStop.setSession(this.getSessionData().getSession());
        requestStop.setObject(this.getSessionData().getResult());
        requestStop.setSeq(this.getSeq());

        String sessionReturn = "";
        try {
            Response response = this._post( "RPC2", requestStop);
            sessionReturn = response.body().string();
        } catch (IOException e) {
            throw new PtzSessionDahuaException(e);
        }

        DahuaResponseStartStop responseStop = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            responseStop = mapper.readValue(sessionReturn, DahuaResponseStartStop.class);
        } catch (JsonProcessingException e) {
            throw new PtzSessionDahuaException(e);
        }

        if(responseStop.getError() != null)
            throw new PtzSessionDahuaException(responseStop.getError().getMessage());

        this.getSessionData()
                .updateSession(
                        responseStop.getId()
                );
    }

    /////// PUBLIC METHODS ///////////

    @Override
    public boolean isConnected() {
        return this._sessionData.isConnected();
    }

    @Override
    public void connect() throws PtzSessionException {
        this.startSession();
        this.login();
        this.factoryInstance();
    }

    @Override
    public void loadPreset(int id) throws PtzSessionException {
        this.ptzStart(
                "GotoPreset",
                id,
                0,
                0,
                0,
                null);
    }

    @Override
    public void savePreset(int id) throws PtzSessionException {
        this.ptzStart(
                "SetPreset",
                id,
                0,
                0,
                0,
                null);
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

    @Override
    public void startZoomIn(int amount) throws PtzSessionException {
        this.ptzStart(
                "ZoomTele",
                amount,
                0,
                0,
                0,
                null);
    }

    @Override
    public void stopZoomIn(int amount) throws PtzSessionException {
        this.ptzStop(
                "ZoomTele",
                amount,
                0,
                0,
                0,
                null);
    }

    @Override
    public void startZoomOut(int amount) throws PtzSessionException {
        this.ptzStart(
                "ZoomWide",
                amount,
                0,
                0,
                0,
                null);
    }

    @Override
    public void stopZoomOut(int amount) throws PtzSessionException {
        this.ptzStop(
                "ZoomWide",
                amount,
                0,
                0,
                0,
                null);
    }

    @Override
    public void startFocusIn(int amount ) throws PtzSessionException {
        this.ptzStart(
                "FocusNear",
                amount,
                0,
                0,
                0,
                null);
    }

    @Override
    public void stopFocusIn(int amount ) throws PtzSessionException {
        this.ptzStop(
                "FocusNear",
                amount,
                0,
                0,
                0,
                null);
    }

    @Override
    public void startFocusOut(int amount ) throws PtzSessionException {
        this.ptzStart(
                "FocusFar",
                amount,
                0,
                0,
                0,
                null);
    }

    @Override
    public void stopFocusOut(int amount ) throws PtzSessionException {
        this.ptzStop(
                "FocusFar",
                amount,
                0,
                0,
                0,
                null);
    }

    @Override
    public void startJoystick(String direction, int speed1, int speed2) throws PtzSessionException {
        this.ptzStart(
                direction,
                speed1,
                speed2,
                0,
                0,
                null);
    }

    @Override
    public void stopJoystick(String direction, int speed1, int speed2) throws PtzSessionException {
        this.ptzStop(
                direction,
                speed1,
                speed2,
                0,
                0,
                null);
    }

    @Override
    public void stopLastCall() throws PtzSessionDahuaException {

        if(this.lastCall != null) {

            DahuaRequestStop requestStop = new DahuaRequestStop();
            requestStop.setParams(this.lastCall.getParams());
            //
            requestStop.setId(this.getSessionData().getNextId());
            requestStop.setSession(this.getSessionData().getSession());
            requestStop.setObject(this.getSessionData().getResult());
            requestStop.setSeq(this.getSeq());

            String sessionReturn = "";
            try {
                Response response = this._post("RPC2", requestStop);
                sessionReturn = response.body().string();
            } catch (Exception e) {
                throw new PtzSessionDahuaException(e);
            }

            DahuaResponseStartStop responseStop = null;
            try {
                ObjectMapper mapper = new ObjectMapper();
                responseStop = mapper.readValue(sessionReturn, DahuaResponseStartStop.class);
            } catch (JsonProcessingException e) {
                throw new PtzSessionDahuaException(e);
            }

            if(responseStop.getError() != null)
                throw new PtzSessionDahuaException(responseStop.getError().getMessage());

            this.getSessionData()
                    .updateSession(
                            responseStop.getId()
                    );
        }

    }


    ///////////// UNDER DEVELOPMENT //////////////

    @Override
    public void specificPosition(int horizontal, int vertical, int zoom) {
        /*
        //let degree = -18.99666 + 6.9773 * Math.log(horizontal);
        return this._ptzStart("PositionABS", horizontal, vertical, zoom);

         */
    }

    @Override
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

    @Override
    public void setConfig(List<DahuaParamRequestSetConfig> setConfigList) throws PtzSessionDahuaException {

        DahuaResponseBase responseSetConfig = null;

        // processing request
        if(setConfigList.size() == 1)
        {
            DahuaRequestSetConfig requestSetConfig = new DahuaRequestSetConfig();
            requestSetConfig.setParams(setConfigList.get(0));
            //
            requestSetConfig.setId(this.getSessionData().getNextId());
            requestSetConfig.setSession(this.getSessionData().getSession());

            String sessionReturn = "";
            try {
                Response response = this._post( "RPC2", requestSetConfig);
                sessionReturn = response.body().string();
            } catch (Exception e) {
                throw new PtzSessionDahuaException(e);
            }

            try {
                ObjectMapper mapper = new ObjectMapper();
                responseSetConfig = mapper.readValue(sessionReturn, DahuaResponseSetConfig.class);
            } catch (JsonProcessingException e) {
                throw new PtzSessionDahuaException(e);
            }

        }
        else if(setConfigList.size() > 1)
        {
            AtomicInteger id = new AtomicInteger(this.getSessionData().getNextId());
            String session = this.getSessionData().getSession();

            DahuaRequestMultiConfig requestMultiConfig = new DahuaRequestMultiConfig();
            requestMultiConfig.setParams(setConfigList
                    .stream()
                    .map(setConfigItem -> {
                        return new DahuaRequestSetConfig(){{
                            setParams(setConfigItem);
                            setId(id.getAndIncrement());
                            setSession(session);
                        }};
                    })
                    .collect(Collectors.toCollection(ArrayList<DahuaRequestBase>::new))
            );
            //
            requestMultiConfig.setId(id.getAndIncrement());
            requestMultiConfig.setSession(session);

            String sessionReturn = "";
            try {
                Response response = this._post( "RPC2", requestMultiConfig);
                sessionReturn = response.body().string();
            } catch (Exception e) {
                throw new PtzSessionDahuaException(e);
            }

            try {
                ObjectMapper mapper = new ObjectMapper();
                responseSetConfig = mapper.readValue(sessionReturn, DahuaResponseMultiConfig.class);
            } catch (JsonProcessingException e) {
                throw new PtzSessionDahuaException(e);
            }
        }

        // error handling
        if(setConfigList.size() > 0) {

            if (responseSetConfig.getError() != null)
                throw new PtzSessionDahuaException(responseSetConfig.getError().getMessage());

            this.getSessionData()
                    .updateSession(
                            responseSetConfig.getId()
                    );
        }
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

    @Override
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