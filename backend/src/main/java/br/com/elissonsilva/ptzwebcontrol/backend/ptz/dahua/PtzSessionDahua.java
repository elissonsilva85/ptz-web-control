package br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua;

import br.com.elissonsilva.ptzwebcontrol.backend.entity.DahuaSessionData;
import br.com.elissonsilva.ptzwebcontrol.backend.exception.PtzSessionException;
import br.com.elissonsilva.ptzwebcontrol.backend.ptz.PtzJoystickDirection;
import br.com.elissonsilva.ptzwebcontrol.backend.ptz.PtzSessionAbstract;
import br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity.*;
import br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity.config.*;
import br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity.param.DahuaParamRequestGetConfig;
import br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity.param.DahuaParamRequestSetConfig;
import br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity.param.DahuaParamResponseGetPresetPresets;
import br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity.param.DahuaParamResponseGetViewRangeStatus;
import br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.exception.PtzSessionDahuaException;
import br.com.elissonsilva.ptzwebcontrol.backend.utils.PtzWebControlUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.Response;

import jakarta.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class PtzSessionDahua extends PtzSessionAbstract {

    private Semaphore mutex;

    private DahuaSessionData _sessionData;

    private DahuaRequestStart lastCall;

    private PtzSessionDahuaKeepAlive ptzSessionDahuaKeepAliveThread;

    private final String INVALID_SESSION_IN_REQUEST_DATA = "Invalid session in request data!";

    public PtzSessionDahua(String ptz, String user, String pass, String url) {
        super(ptz, user, pass, url);
        mutex = new Semaphore(1);
    }

    private Response _post(String page, DahuaRequestBase requestBase ) throws PtzSessionException {
        if(requestBase instanceof DahuaRequestStart)
            this.lastCall = (DahuaRequestStart) requestBase;

        page = this._getUrl(page);
        String body;
        try {
            ObjectMapper mapper = new ObjectMapper();
            body = mapper.writeValueAsString(requestBase);
            return super._post(page, body);
        } catch (JsonProcessingException e) {
            throw new PtzSessionDahuaException(e);
        } catch (IOException e) {
            throw new PtzSessionDahuaException(e.getMessage() + " -> page: " + page, e);
        }
    }

    /////// PRIVATE METHODS /////////////////////

    private DahuaSessionData getSessionData() {
        return _sessionData;
    }

    private void setSessionData(DahuaSessionData _sessionData) {
        this._sessionData = _sessionData;
    }

    private long getSeq() {
        // pega só os numeros
        String session = this._sessionData.getSession();
        String onlyNumbers = session.replaceAll("[^0-9]", "");

        // converte pra numero seguindo o comportamento do parsetInt no Javascript
        // https://262.ecma-international.org/6.0/#sec-parseint-string-radix
        BigInteger num;
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

    private String getHashPassword() throws PtzSessionException {
        return this.hexMD5(
                    this._user + ":" +
                        this._sessionData.getRandom() + ":" +
                        this.hexMD5(this._user + ":" +
                            this._sessionData.getRealm() + ":" +
                            this._pass)).toUpperCase();
    }

    private String hexMD5(String text) throws PtzSessionException {

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(text.getBytes());
            byte[] digest = md.digest();
            return DatatypeConverter
                    .printHexBinary(digest).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            throw new PtzSessionDahuaException(e);
        }

    }

    private void startSession() throws PtzSessionException {

        try {
            mutex.acquire();
        } catch (InterruptedException e) {
            throw new PtzSessionDahuaException("mutex.acquire() error: " + e.getMessage(), e);
        }

        DahuaRequestLogin requestLogin = new DahuaRequestLogin();
        requestLogin.getParams().setUserName(this._user);
        requestLogin.setId(4);

        String sessionReturn = null;
        try(Response response = this._post( "RPC2_Login", requestLogin)) {
            if(response.body() != null)
                sessionReturn = response.body().string();
        } catch (IOException e) {
            mutex.release();
            throw new PtzSessionDahuaException(e);
        }

        if(sessionReturn == null) {
            mutex.release();
            throw new PtzSessionDahuaException("sessionReturn could not be null");
        }

        DahuaResponseLogin responseLogin = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            responseLogin = mapper.readValue(sessionReturn, DahuaResponseLogin.class);
        } catch (JsonProcessingException e) {
            mutex.release();
            throw new PtzSessionDahuaException(e.getMessage() + " -> sessionReturn: " + sessionReturn, e);
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

        mutex.release();
    }

    private void login() throws PtzSessionException {

        try {
            mutex.acquire();
        } catch (InterruptedException e) {
            throw new PtzSessionDahuaException("mutex.acquire() error: " + e.getMessage(), e);
        }

        String hashPass = this.getHashPassword();

        DahuaRequestLogin requestLogin = new DahuaRequestLogin();
        requestLogin.getParams().setUserName(this._user);
        requestLogin.getParams().setPassword(hashPass);
        requestLogin.getParams().setAuthorityType("Default");
        requestLogin.setId(3);
        requestLogin.setSession(this.getSessionData().getSession());

        String sessionReturn = "";
        try(Response response = this._post( "RPC2_Login", requestLogin)) {
            if(response.body() != null)
                sessionReturn = response.body().string();
        } catch (IOException e) {
            mutex.release();
            throw new PtzSessionDahuaException(e);
        }

        if(sessionReturn == null) {
            mutex.release();
            throw new PtzSessionDahuaException("sessionReturn could not be null");
        }

        DahuaResponseLogin responseLogin = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            responseLogin = mapper.readValue(sessionReturn, DahuaResponseLogin.class);
        } catch (JsonProcessingException e) {
            throw new PtzSessionDahuaException(e.getMessage() + " -> sessionReturn: " + sessionReturn, e);
        }

        if(responseLogin.getError() != null) {
            if ("Invalid session in request data!".equals(responseLogin.getError().getMessage()))
                this._sessionData.setConnected(false);
            mutex.release();
            throw new PtzSessionDahuaException(responseLogin.getError().getMessage());
        }

        this.getSessionData()
                .updateSession(
                        responseLogin.getSession(),
                        responseLogin.getId()
                );

        mutex.release();
    }

    private void factoryInstance() throws PtzSessionException {

        try {
            mutex.acquire();
        } catch (InterruptedException e) {
            throw new PtzSessionDahuaException("mutex.acquire() error: " + e.getMessage(), e);
        }

        DahuaRequestFactoryInstance factoryInstance = new DahuaRequestFactoryInstance();
        factoryInstance.setId(3);
        factoryInstance.setSession(this.getSessionData().getSession());

        String sessionReturn = "";
        try(Response response = this._post( "RPC2", factoryInstance)) {
            if(response.body() != null)
                sessionReturn = response.body().string();
        } catch (IOException e) {
            mutex.release();
            throw new PtzSessionDahuaException(e);
        }

        if(sessionReturn == null) {
            mutex.release();
            throw new PtzSessionDahuaException("sessionReturn could not be null");
        }

        DahuaResponseFactoryInstance responseFactoryInstance = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            responseFactoryInstance = mapper.readValue(sessionReturn, DahuaResponseFactoryInstance.class);
        } catch (JsonProcessingException e) {
            mutex.release();
            throw new PtzSessionDahuaException(e.getMessage() + " -> sessionReturn: " + sessionReturn, e);
        }

        if(responseFactoryInstance.getError() != null) {
            if (INVALID_SESSION_IN_REQUEST_DATA.equals(responseFactoryInstance.getError().getMessage()))
                this._sessionData.setConnected(false);
            mutex.release();
            throw new PtzSessionDahuaException(responseFactoryInstance.getError().getMessage());
        }

        this.getSessionData()
                .updateSession(
                        responseFactoryInstance.getId(),
                        responseFactoryInstance.getResult()
                );

        mutex.release();
    }

    private void ptzStart(String code, int arg1, int arg2, int arg3, int arg4, String channel) throws PtzSessionException {

        try {
            mutex.acquire();
        } catch (InterruptedException e) {
            throw new PtzSessionDahuaException("mutex.acquire() error: " + e.getMessage(), e);
        }

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

        String sessionReturn = null;
        try(Response response = this._post( "RPC2", requestStart)) {
            if(response.body() != null)
                sessionReturn = response.body().string();
        } catch (IOException e) {
            mutex.release();
            throw new PtzSessionDahuaException(e);
        }

        if(sessionReturn == null) {
            mutex.release();
            throw new PtzSessionDahuaException("sessionReturn could not be null");
        }

        DahuaResponseStartStop responseStart = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            responseStart = mapper.readValue(sessionReturn, DahuaResponseStartStop.class);
        } catch (JsonProcessingException e) {
            mutex.release();
            throw new PtzSessionDahuaException(e.getMessage() + " -> sessionReturn: " + sessionReturn, e);
        }

        if(responseStart.getError() != null) {
            if (INVALID_SESSION_IN_REQUEST_DATA.equals(responseStart.getError().getMessage()))
                this._sessionData.setConnected(false);
            mutex.release();
            throw new PtzSessionDahuaException(responseStart.getError().getMessage());
        }

        this.getSessionData()
                .updateSession(
                        responseStart.getId()
                );

        mutex.release();
    }

    private void ptzStop(String code, int arg1, int arg2, int arg3, int arg4, String channel) throws PtzSessionException {

        try {
            mutex.acquire();
        } catch (InterruptedException e) {
            throw new PtzSessionDahuaException("mutex.acquire() error: " + e.getMessage(), e);
        }

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

        String sessionReturn = null;
        try(Response response = this._post( "RPC2", requestStop)) {
            if(response.body() != null)
                sessionReturn = response.body().string();
        } catch (IOException e) {
            mutex.release();
            throw new PtzSessionDahuaException(e);
        }

        if(sessionReturn == null) {
            mutex.release();
            throw new PtzSessionDahuaException("sessionReturn could not be null");
        }

        DahuaResponseStartStop responseStop = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            responseStop = mapper.readValue(sessionReturn, DahuaResponseStartStop.class);
        } catch (JsonProcessingException e) {
            mutex.release();
            throw new PtzSessionDahuaException(e.getMessage() + " -> sessionReturn: " + sessionReturn, e);
        }

        if(responseStop.getError() != null) {
            if(INVALID_SESSION_IN_REQUEST_DATA.equals(responseStop.getError().getMessage()))
                this._sessionData.setConnected(false);
            mutex.release();
            throw new PtzSessionDahuaException(responseStop.getError().getMessage());
        }

        this.getSessionData()
                .updateSession(
                        responseStop.getId()
                );

        mutex.release();
    }

    private void setPreset(int preset, String name) throws PtzSessionDahuaException {

        try {
            mutex.acquire();
        } catch (InterruptedException e) {
            throw new PtzSessionDahuaException("mutex.acquire() error: " + e.getMessage(), e);
        }

        DahuaRequestSetPreset requestSetPreset = new DahuaRequestSetPreset();
        requestSetPreset.setId(this.getSessionData().getNextId());
        requestSetPreset.setSession(this.getSessionData().getSession());
        requestSetPreset.setObject(this.getSessionData().getResult());
        requestSetPreset.setSeq(this.getSeq());
        //
        requestSetPreset.getParams().setIndex(preset);
        requestSetPreset.getParams().setName(name);
        //

        String sessionReturn = null;
        try(Response response = this._post( "RPC2", requestSetPreset)) {
            if(response.body() != null)
                sessionReturn = response.body().string();
        } catch (IOException | PtzSessionException e) {
            mutex.release();
            throw new PtzSessionDahuaException(e);
        }

        if(sessionReturn == null) {
            mutex.release();
            throw new PtzSessionDahuaException("sessionReturn could not be null");
        }

        DahuaResponseGetPreset responseStart = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            responseStart = mapper.readValue(sessionReturn, DahuaResponseGetPreset.class);
        } catch (JsonProcessingException e) {
            mutex.release();
            throw new PtzSessionDahuaException(e.getMessage() + " -> sessionReturn: " + sessionReturn, e);
        }

        if(responseStart.getError() != null) {
            if (INVALID_SESSION_IN_REQUEST_DATA.equals(responseStart.getError().getMessage()))
                this._sessionData.setConnected(false);
            mutex.release();
            throw new PtzSessionDahuaException(responseStart.getError().getMessage());
        }

        this.getSessionData()
                .updateSession(
                        responseStart.getId()
                );

        mutex.release();
    }

    private List<DahuaParamResponseGetPresetPresets> getPresets() throws PtzSessionDahuaException {

        try {
            mutex.acquire();
        } catch (InterruptedException e) {
            throw new PtzSessionDahuaException("mutex.acquire() error: " + e.getMessage(), e);
        }

        DahuaRequestGetPreset requestGetPreset = new DahuaRequestGetPreset();
        requestGetPreset.setId(this.getSessionData().getNextId());
        requestGetPreset.setSession(this.getSessionData().getSession());
        requestGetPreset.setObject(this.getSessionData().getResult());
        requestGetPreset.setSeq(this.getSeq());
        //

        String sessionReturn = null;
        try(Response response = this._post( "RPC2", requestGetPreset)) {
            if(response.body() != null)
                sessionReturn = response.body().string();
        } catch (IOException | PtzSessionException e) {
            mutex.release();
            throw new PtzSessionDahuaException(e);
        }

        if(sessionReturn == null) {
            mutex.release();
            throw new PtzSessionDahuaException("sessionReturn could not be null");
        }

        DahuaResponseGetPreset responseStart = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            responseStart = mapper.readValue(sessionReturn, DahuaResponseGetPreset.class);
        } catch (JsonProcessingException e) {
            mutex.release();
            throw new PtzSessionDahuaException(e.getMessage() + " -> sessionReturn: " + sessionReturn, e);
        }

        if(responseStart.getError() != null) {
            if (INVALID_SESSION_IN_REQUEST_DATA.equals(responseStart.getError().getMessage()))
                this._sessionData.setConnected(false);
            mutex.release();
            throw new PtzSessionDahuaException(responseStart.getError().getMessage());
        }

        this.getSessionData()
                .updateSession(
                        responseStart.getId()
                );

        mutex.release();

        return responseStart.getParams().getPresets();
    }


    /////// PUBLIC METHODS ///////////

    public void keepAlive() throws PtzSessionException {

        try {
            mutex.acquire();
        } catch (InterruptedException e) {
            throw new PtzSessionDahuaException("mutex.acquire() error: " + e.getMessage(), e);
        }

        DahuaRequestKeepAlive keepAlive = new DahuaRequestKeepAlive();
        keepAlive.setId(this.getSessionData().getNextId());
        keepAlive.setSession(this.getSessionData().getSession());

        String sessionReturn = "";
        try(Response response = this._post( "RPC2", keepAlive)) {
            if(response.body() != null)
                sessionReturn = response.body().string();
        } catch (IOException e) {
            mutex.release();
            throw new PtzSessionDahuaException(e);
        }

        if(sessionReturn == null) {
            mutex.release();
            throw new PtzSessionDahuaException("sessionReturn could not be null");
        }

        DahuaResponseKeepAlive responseKeepAlive = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            responseKeepAlive = mapper.readValue(sessionReturn, DahuaResponseKeepAlive.class);
        } catch (JsonProcessingException e) {
            mutex.release();
            throw new PtzSessionDahuaException(e.getMessage() + " -> sessionReturn: " + sessionReturn, e);
        }

        if(responseKeepAlive.getError() != null) {
            if (INVALID_SESSION_IN_REQUEST_DATA.equals(responseKeepAlive.getError().getMessage()))
                this._sessionData.setConnected(false);
            mutex.release();
            throw new PtzSessionDahuaException(responseKeepAlive.getError().getMessage());
        }

        this.getSessionData()
                .updateSession(
                        responseKeepAlive.getId()
                );

        mutex.release();
    }

    @Override
    public boolean isConnected() {
        if(this._sessionData == null) return false;
        return this._sessionData.isConnected();
    }

    @Override
    public void connect() throws PtzSessionException {
        if(!this.isConnected()) {
            this.startSession();
            this.login();
            this.factoryInstance();
        }

        if(ptzSessionDahuaKeepAliveThread == null) {
            ptzSessionDahuaKeepAliveThread = new PtzSessionDahuaKeepAlive();
            ptzSessionDahuaKeepAliveThread.setSession(this);
        }

        if(!ptzSessionDahuaKeepAliveThread.isAlive()) {
            ptzSessionDahuaKeepAliveThread.start();
        }
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
    public void savePreset(int id, String name) throws PtzSessionException {
        this.setPreset(id, name);
    }

    @Override
    public void setZoomSpeed(int value) throws PtzSessionDahuaException {

        List<List<DahuaParamConfigVideoInZoom>> list = Arrays.asList(
                Arrays.asList(
                        new DahuaParamConfigVideoInZoom() {{
                            setSpeed(value);
                        }},
                        new DahuaParamConfigVideoInZoom() {{
                            setSpeed(value);
                        }},
                        new DahuaParamConfigVideoInZoom() {{
                            setSpeed(value);
                        }}
                )
        );

        DahuaParamRequestSetConfig setConfig = new DahuaParamRequestSetConfig();
        setConfig.setName("VideoInZoom");
        setConfig.setTable(list);

        this.setConfig(Arrays.asList(setConfig));
    }

    @Override
    public int getZoomValue() throws PtzSessionDahuaException {

        try {
            mutex.acquire();
        } catch (InterruptedException e) {
            throw new PtzSessionDahuaException("mutex.acquire() error: " + e.getMessage(), e);
        }

        DahuaRequestGetZoom requestGetZoom = new DahuaRequestGetZoom();
        requestGetZoom.setId(this.getSessionData().getNextId());
        requestGetZoom.setSession(this.getSessionData().getSession());
        requestGetZoom.setObject(this.getSessionData().getResult());
        requestGetZoom.setSeq(this.getSeq());
        //

        String sessionReturn = null;
        try(Response response = this._post( "RPC2", requestGetZoom)) {
            if(response.body() != null)
                sessionReturn = response.body().string();
        } catch (IOException | PtzSessionException e) {
            mutex.release();
            throw new PtzSessionDahuaException(e);
        }

        if(sessionReturn == null) {
            mutex.release();
            throw new PtzSessionDahuaException("sessionReturn could not be null");
        }

        int zoomValueConverted = 0;
        DahuaResponseGetZoom responseStart = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            responseStart = mapper.readValue(sessionReturn, DahuaResponseGetZoom.class);
            zoomValueConverted = PtzWebControlUtils.speedConverter(responseStart.getParams().getValue(), 10, 300, 1, 128);
        } catch (Exception e) {
            mutex.release();
            throw new PtzSessionDahuaException(e.getMessage() + " -> sessionReturn: " + sessionReturn, e);
        }

        if(responseStart.getError() != null) {
            if (INVALID_SESSION_IN_REQUEST_DATA.equals(responseStart.getError().getMessage()))
                this._sessionData.setConnected(false);
            mutex.release();
            throw new PtzSessionDahuaException(responseStart.getError().getMessage());
        }

        this.getSessionData()
                .updateSession(
                        responseStart.getId()
                );

        mutex.release();
        return zoomValueConverted;
    }

    @Override
    public int[] getViewAngles() throws PtzSessionDahuaException {

        try {
            mutex.acquire();
        } catch (InterruptedException e) {
            throw new PtzSessionDahuaException("mutex.acquire() error: " + e.getMessage(), e);
        }

        DahuaRequestGetViewRange requestGetZoom = new DahuaRequestGetViewRange();
        requestGetZoom.setId(this.getSessionData().getNextId());
        requestGetZoom.setSession(this.getSessionData().getSession());
        requestGetZoom.setObject(this.getSessionData().getResult());
        requestGetZoom.setSeq(this.getSeq());
        //

        String sessionReturn = null;
        try(Response response = this._post( "RPC2", requestGetZoom)) {
            if(response.body() != null)
                sessionReturn = response.body().string();
        } catch (IOException | PtzSessionException e) {
            mutex.release();
            throw new PtzSessionDahuaException(e);
        }

        if(sessionReturn == null) {
            mutex.release();
            throw new PtzSessionDahuaException("sessionReturn could not be null");
        }

        DahuaResponseGetViewRange responseStart = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            responseStart = mapper.readValue(sessionReturn, DahuaResponseGetViewRange.class);
        } catch (JsonProcessingException e) {
            mutex.release();
            throw new PtzSessionDahuaException(e.getMessage() + " -> sessionReturn: " + sessionReturn, e);
        }

        if(responseStart.getError() != null) {
            if (INVALID_SESSION_IN_REQUEST_DATA.equals(responseStart.getError().getMessage()))
                this._sessionData.setConnected(false);
            mutex.release();
            throw new PtzSessionDahuaException(responseStart.getError().getMessage());
        }

        this.getSessionData()
                .updateSession(
                        responseStart.getId()
                );

        mutex.release();

        DahuaParamResponseGetViewRangeStatus status = responseStart.getParams().getStatus();
        int horizontal = Math.round(( status.getAzimuthH() >= 0.5 ?
                            status.getAzimuthH() - (float) 0.5 :
                            status.getAzimuthH() + (float) 1.5
                        ) * 10 * 180);
        int vertical = Math.round(Math.abs(status.getAzimuthV()) * 10 * 180);

        return new int[]{ horizontal, vertical };

    }

    public Map<Integer, String> getPresetNames() throws PtzSessionException {

        List<DahuaParamResponseGetPresetPresets> presetNames = this.getPresets();

        Map<Integer, String> result = new HashMap<>();
        for(DahuaParamResponseGetPresetPresets preset : presetNames) {
            result.put(preset.getIndex(), preset.getName());
        }

        return result;
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
    public void startIrisLarge(int amount) throws PtzSessionException {
        this.ptzStart(
                "IrisLarge",
                amount,
                0,
                0,
                0,
                null);
    }

    @Override
    public void stopIrisLarge(int amount) throws PtzSessionException {
        this.ptzStop(
                "IrisLarge",
                amount,
                0,
                0,
                0,
                null);
    }

    @Override
    public void startIrisSmall(int amount) throws PtzSessionException {
        this.ptzStart(
                "IrisSmall",
                amount,
                0,
                0,
                0,
                null);
    }

    @Override
    public void stopIrisSmall(int amount) throws PtzSessionException {
        this.ptzStop(
                "IrisSmall",
                amount,
                0,
                0,
                0,
                null);
    }

    @Override
    public void startJoystick(PtzJoystickDirection direction, int speed1, int speed2) throws PtzSessionException {
        this.ptzStart(
                direction.toString(),
                speed1,
                speed2,
                0,
                0,
                null);
    }

    @Override
    public void stopJoystick(PtzJoystickDirection direction, int speed1, int speed2) throws PtzSessionException {
        this.ptzStop(
                direction.toString(),
                speed1,
                speed2,
                0,
                0,
                null);
    }

    @Override
    public void stopLastCall() throws PtzSessionDahuaException {

        if(this.lastCall != null) {

            try {
                mutex.acquire();
            } catch (InterruptedException e) {
                throw new PtzSessionDahuaException("mutex.acquire() error: " + e.getMessage(), e);
            }

            DahuaRequestStop requestStop = new DahuaRequestStop();
            requestStop.setParams(this.lastCall.getParams());
            //
            requestStop.setId(this.getSessionData().getNextId());
            requestStop.setSession(this.getSessionData().getSession());
            requestStop.setObject(this.getSessionData().getResult());
            requestStop.setSeq(this.getSeq());

            String sessionReturn = "";
            try(Response response = this._post("RPC2", requestStop)) {
                if(response.body() != null)
                    sessionReturn = response.body().string();
            } catch (Exception e) {
                mutex.release();
                throw new PtzSessionDahuaException(e);
            }

            DahuaResponseStartStop responseStop = null;
            try {
                ObjectMapper mapper = new ObjectMapper();
                responseStop = mapper.readValue(sessionReturn, DahuaResponseStartStop.class);
            } catch (JsonProcessingException e) {
                mutex.release();
                throw new PtzSessionDahuaException(e);
            }

            if(responseStop.getError() != null) {
                if(INVALID_SESSION_IN_REQUEST_DATA.equals(responseStop.getError().getMessage()))
                    this._sessionData.setConnected(false);
                mutex.release();
                throw new PtzSessionDahuaException(responseStop.getError().getMessage());
            }

            this.getSessionData()
                    .updateSession(
                            responseStop.getId()
                    );

            mutex.release();
        }

    }


    ///////////// UNDER DEVELOPMENT //////////////

    @Override
    public void specificPosition(int horizontal, int vertical, int zoom) throws PtzSessionException {

        this.ptzStart(
                "PositionABS",
                horizontal,
                vertical,
                zoom,
                0,
                null);
    }

    public String getConfig(List<String> getConfigList) throws PtzSessionDahuaException {

        try {
            mutex.acquire();
        } catch (InterruptedException e) {
            throw new PtzSessionDahuaException("mutex.acquire() error: " + e.getMessage(), e);
        }

        DahuaResponseMultiGetConfig responseGetConfig = null;
        AtomicInteger id = new AtomicInteger(this.getSessionData().getNextId());
        String session = this.getSessionData().getSession();

        DahuaRequestMultiConfig requestMultiConfig = new DahuaRequestMultiConfig();
        requestMultiConfig.setParams(getConfigList
                .stream()
                .map(getConfigItem -> {
                    return new DahuaRequestGetConfig(){{
                        setParams(new DahuaParamRequestGetConfig(){{
                            setName(getConfigItem);
                        }});
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
        try(Response response = this._post( "RPC2", requestMultiConfig)) {
            if(response.body() != null)
                sessionReturn = response.body().string();
        } catch (Exception e) {
            mutex.release();
            throw new PtzSessionDahuaException(e);
        }

        this.getSessionData()
                .updateSession(
                        id.get()
                );

        mutex.release();

        return sessionReturn;
    }

    public void setConfig(List<DahuaParamRequestSetConfig> setConfigList) throws PtzSessionDahuaException {

        try {
            mutex.acquire();
        } catch (InterruptedException e) {
            throw new PtzSessionDahuaException("mutex.acquire() error: " + e.getMessage(), e);
        }

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
            try(Response response = this._post( "RPC2", requestSetConfig)) {
                if(response.body() != null)
                    sessionReturn = response.body().string();
            } catch (Exception e) {
                mutex.release();
                throw new PtzSessionDahuaException(e);
            }

            try {
                ObjectMapper mapper = new ObjectMapper();
                responseSetConfig = mapper.readValue(sessionReturn, DahuaResponseSetConfig.class);
            } catch (JsonProcessingException e) {
                mutex.release();
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
            try(Response response = this._post( "RPC2", requestMultiConfig)) {
                if(response.body() != null)
                    sessionReturn = response.body().string();
            } catch (Exception e) {
                mutex.release();
                throw new PtzSessionDahuaException(e);
            }

            try {
                ObjectMapper mapper = new ObjectMapper();
                responseSetConfig = mapper.readValue(sessionReturn, DahuaResponseMultiSetConfig.class);
            } catch (JsonProcessingException e) {
                mutex.release();
                throw new PtzSessionDahuaException(e);
            }
        }

        // error handling
        if(setConfigList.size() > 0) {

            if(responseSetConfig.getError() != null) {
                if (INVALID_SESSION_IN_REQUEST_DATA.equals(responseSetConfig.getError().getMessage()))
                    this._sessionData.setConnected(false);
                mutex.release();
                throw new PtzSessionDahuaException(responseSetConfig.getError().getMessage());
            }

            this.getSessionData()
                    .updateSession(
                            responseSetConfig.getId()
                    );
        }

        mutex.release();
    }

    public void setTemporaryConfig(DahuaParamRequestSetConfig setConfigList) throws PtzSessionDahuaException {

        try {
            mutex.acquire();
        } catch (InterruptedException e) {
            throw new PtzSessionDahuaException("mutex.acquire() error: " + e.getMessage(), e);
        }

        DahuaRequestSetTemporaryConfig requestSetConfig = new DahuaRequestSetTemporaryConfig();
        requestSetConfig.setParams(setConfigList);
        //
        requestSetConfig.setId(this.getSessionData().getNextId());
        requestSetConfig.setSession(this.getSessionData().getSession());

        String sessionReturn = null;
        try(Response response = this._post( "RPC2", requestSetConfig)) {
            if(response.body() != null)
                sessionReturn = response.body().string();
        } catch (Exception e) {
            mutex.release();
            throw new PtzSessionDahuaException(e);
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            DahuaResponseSetTemporaryConfig responseSetConfig = mapper.readValue(sessionReturn, DahuaResponseSetTemporaryConfig.class);

            if(responseSetConfig.getError() != null) {
                if (INVALID_SESSION_IN_REQUEST_DATA.equals(responseSetConfig.getError().getMessage()))
                    this._sessionData.setConnected(false);
                mutex.release();
                throw new PtzSessionDahuaException(responseSetConfig.getError().getMessage());
            }

            this.getSessionData()
                    .updateSession(
                            responseSetConfig.getId()
                    );

            mutex.release();

        } catch (JsonProcessingException e) {
            mutex.release();
            throw new PtzSessionDahuaException(e);
        }

    }

    public void setVideoInFocus(int mode, boolean temporary) throws PtzSessionDahuaException {

        List<List<DahuaParamConfigVideoInFocus>> list = Arrays.asList(
                Arrays.asList(
                        new DahuaParamConfigVideoInFocus() {{
                            setMode(mode);
                        }},
                        new DahuaParamConfigVideoInFocus() {{
                            setMode(mode);
                        }},
                        new DahuaParamConfigVideoInFocus() {{
                            setMode(mode);
                        }}
                )
        );

        DahuaParamRequestSetConfig setConfig = new DahuaParamRequestSetConfig();
        setConfig.setName("VideoInFocus");
        setConfig.setTable(list);

        if(temporary) {
            this.setTemporaryConfig(setConfig);
        }
        else {
            this.setConfig(Arrays.asList(setConfig));
        }
    }

    public void setVideoColor(boolean temporary) throws PtzSessionDahuaException {
        List<List<DahuaParamConfigVideoColorTable>> list = Arrays.asList(
                Arrays.asList(
                        new DahuaParamConfigVideoColorTable(),
                        new DahuaParamConfigVideoColorTable(),
                        new DahuaParamConfigVideoColorTable()
                )
        );

        DahuaParamRequestSetConfig setConfig = new DahuaParamRequestSetConfig();
        setConfig.setName("VideoColor");
        setConfig.setTable(list);

        if(temporary) {
            this.setTemporaryConfig(setConfig);
        }
        else {
            this.setConfig(Arrays.asList(setConfig));
        }

    }

    public void setVideoInMode(int config, boolean temporary) throws PtzSessionDahuaException {

        List<DahuaParamConfigVideoInMode> list = Arrays.asList(
                new DahuaParamConfigVideoInMode(){{
                    setConfig(new int[]{ config });
                }}
        );

        DahuaParamRequestSetConfig setConfig = new DahuaParamRequestSetConfig();
        setConfig.setName("VideoInMode");
        setConfig.setTable(list);

        if(temporary) {
            this.setTemporaryConfig(setConfig);
        }
        else {
            this.setConfig(Arrays.asList(setConfig));
        }

    }

    public void setVideoInWhiteBalance(boolean temporary) throws PtzSessionDahuaException {

        List<List<DahuaParamConfigVideoInWhiteBalance>> list = Arrays.asList(
                Arrays.asList(
                        new DahuaParamConfigVideoInWhiteBalance() {{
                            setColorTemperatureLevel(50);
                            setGainBlue(50);
                            setGainGreen(50);
                            setGainRed(50);
                            setMode("Auto");
                        }},
                        new DahuaParamConfigVideoInWhiteBalance() {{
                            setColorTemperatureLevel(50);
                            setGainBlue(50);
                            setGainGreen(50);
                            setGainRed(50);
                            setMode("Auto");
                        }},
                        new DahuaParamConfigVideoInWhiteBalance() {{
                            setColorTemperatureLevel(50);
                            setGainBlue(50);
                            setGainGreen(50);
                            setGainRed(50);
                            setMode("Auto");
                        }}
                )
        );

        DahuaParamRequestSetConfig setConfig = new DahuaParamRequestSetConfig();
        setConfig.setName("VideoInWhiteBalance");
        setConfig.setTable(list);

        if(temporary) {
            this.setTemporaryConfig(setConfig);
        }
        else {
            this.setConfig(Arrays.asList(setConfig));
        }
    }

    public void moveDirectly(int[] coord) {

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
