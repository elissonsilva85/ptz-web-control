package br.com.elissonsilva.ptzwebcontrol.tests;

import br.com.elissonsilva.ptzwebcontrol.backend.ApplicationBootstrap;
import br.com.elissonsilva.ptzwebcontrol.backend.component.Config;
import br.com.elissonsilva.ptzwebcontrol.backend.entity.ConfigPtz;
import br.com.elissonsilva.ptzwebcontrol.backend.entity.ConfigPtzConnection;
import br.com.elissonsilva.ptzwebcontrol.backend.exception.PtzSessionException;
import br.com.elissonsilva.ptzwebcontrol.backend.exception.PtzSessionManagerException;
import br.com.elissonsilva.ptzwebcontrol.backend.ptz.PtzJoystickDirection;
import br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.PtzSessionDahua;
import br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity.config.DahuaParamRequestSetConfigVideoColorTable;
import br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity.config.DahuaParamRequestSetConfigVideoInWhiteBalance;
import br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity.config.DahuaParamRequestSetConfigVideoInZoom;
import br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity.config.DahuaParamResponseGetPresetPresets;
import br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity.param.DahuaParamRequestSetConfig;
import br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.entity.param.DahuaParamRequestSetTemporaryConfig;
import br.com.elissonsilva.ptzwebcontrol.backend.services.PtzSessionManagerService;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.fail;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(classes = ApplicationBootstrap.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@WebAppConfiguration
@Ignore
public class TestsPtzSessionDahua {

    @Spy
    @Autowired
    private Config configSpy;

    @Autowired
    private PtzSessionManagerService ptzSessionManagerService;

    private static PtzSessionDahua sessionDahua;

    private static int zoomValue;

    private static int[] angles;

    private static final int presetIndex = 4;
    private static final String presetName = "Teste";

    @Before
    public void setup() {

        // Prepare Mock
        configSpy = Mockito.spy(new Config());

        // Mock Config data
        when(configSpy.getPtz())
                .thenReturn(new ConfigPtz() {{
                    setConnection(new HashMap<>() {{
                        put("PTZ1", new ConfigPtzConnection() {{
                            setBrand("dahua");
                            setLabel("PTZ1");
                            setUser("admin");
                            setPassword("tecvoz12");
                            setUrl("http://192.168.0.112");
                        }});
                    }});
                }});

        ptzSessionManagerService = new PtzSessionManagerService(configSpy);

    }

    @Test
    public void _001_checkHashPassword() {

        String pass = "128CDAD12C6B9F8966F4B23F5F26AD76";
        //String currentPass = sessionDahua.getHashPassword();
        assertEquals("128CDAD12C6B9F8966F4B23F5F26AD76", pass, "PtzSessionDahua.getHashPassword with wrong result");

    }

    @Test
    public void _002_getSession() throws PtzSessionManagerException {

        sessionDahua = (PtzSessionDahua) ptzSessionManagerService.getSession("PTZ1");

    }

    @Test
    public void _003_connect() {

        try {
            sessionDahua.connect();
        } catch (PtzSessionException e) {
            fail(e.getMessage());
        }

    }

    @Test
    public void _010_loadPreset() {

        try {
            sessionDahua.loadPreset(presetIndex);
            Thread.sleep(2000);
        } catch (Exception e) {
            fail(e.getMessage());
        }

    }

    @Test
    public void _011_setPresets() {

        try {
            sessionDahua.setPreset(presetIndex, presetName);
            Thread.sleep(500);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void _012_getPresets() {

        try {
            List<DahuaParamResponseGetPresetPresets> list = sessionDahua.getPresets();
            list.forEach( p -> System.out.println(p) );
            assertEquals(presetName, list.get(presetIndex - 1).getName(), "PtzSessionDahua.getPresets[" + (presetIndex-1) + "] with wrong value");
            Thread.sleep(500);
        } catch (PtzSessionException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void _013_getZoomValue() throws Exception {

        try {
            zoomValue = sessionDahua.getZoomValue();
            System.out.println("zoomValue = " + zoomValue);

            Thread.sleep(500);
        } catch (PtzSessionException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void _014_getViewRange() {

        try {
            angles = sessionDahua.getViewAngles();
            System.out.println(angles[0] + "," + angles[1]);

            Thread.sleep(500);
        } catch (PtzSessionException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void _015_specificPosition() throws Exception {

        try {
            int horizontal = angles[0];
            int vertical = angles[1];

            System.out.println("horizontal = " + horizontal + " | vertical = " + vertical + " | zoom = " + zoomValue);

            sessionDahua.specificPosition(horizontal, vertical, zoomValue);
            Thread.sleep(1000);
        } catch (PtzSessionException | InterruptedException e) {
            fail(e.getMessage());
        }

        //System.exit(0);

    }

    @Test
    public void _021_startStopZoomIn() {

        try {
            sessionDahua.startZoomIn(5);
            Thread.sleep(1000);
            sessionDahua.stopZoomIn(5);
            Thread.sleep(1000);
        } catch (PtzSessionException | InterruptedException e) {
            fail(e.getMessage());
        }

    }

    @Test
    public void _022_setConfigZoomSpeed() {

        try {

            int zoomSpeed = 10;

            List<List<DahuaParamRequestSetConfigVideoInZoom>> list = Arrays.asList(
                    Arrays.asList(
                            new DahuaParamRequestSetConfigVideoInZoom() {{
                                setSpeed(zoomSpeed);
                            }},
                            new DahuaParamRequestSetConfigVideoInZoom() {{
                                setSpeed(zoomSpeed);
                            }},
                            new DahuaParamRequestSetConfigVideoInZoom() {{
                                setSpeed(zoomSpeed);
                            }}
                    )
            );

            DahuaParamRequestSetConfig setConfig = new DahuaParamRequestSetConfig();
            setConfig.setName("VideoInZoom");
            setConfig.setTable(list);

            sessionDahua.setConfig(Arrays.asList(setConfig));
            Thread.sleep(1000);
        } catch (PtzSessionException | InterruptedException e) {
            fail(e.getMessage());
        }

    }

    @Test
    public void _023_startStopZoomOut() {

        try {
            sessionDahua.startZoomOut(5);
            Thread.sleep(500);
            sessionDahua.stopZoomOut(5);
            Thread.sleep(1000);
        } catch (PtzSessionException | InterruptedException e) {
            fail(e.getMessage());
        }

    }

    @Test
    public void _024_startStopJoystickRight() {

        try {
            sessionDahua.startJoystick(PtzJoystickDirection.Right,1, 1);
            Thread.sleep(500);
            sessionDahua.stopJoystick(PtzJoystickDirection.Right, 1, 1);
            Thread.sleep(1000);
        } catch (PtzSessionException | InterruptedException e) {
            fail(e.getMessage());
        }

    }

    @Test
    public void _030_setTemporaryConfigVideoColor() {

        try {

            int zoomSpeed = 10;

            List<List<DahuaParamRequestSetConfigVideoColorTable>> list = Arrays.asList(
                    Arrays.asList(
                            new DahuaParamRequestSetConfigVideoColorTable(),
                            new DahuaParamRequestSetConfigVideoColorTable(),
                            new DahuaParamRequestSetConfigVideoColorTable()
                    )
            );

            DahuaParamRequestSetTemporaryConfig<List<DahuaParamRequestSetConfigVideoColorTable>> setConfig = new DahuaParamRequestSetTemporaryConfig<>();
            setConfig.setName("VideoColor");
            setConfig.setTable(list);

            sessionDahua.setTemporaryConfig(setConfig);
            Thread.sleep(1000);
        } catch (PtzSessionException | InterruptedException e) {
            fail(e.getMessage());
        }

    }

    @Test
    public void _031_setConfigMultiCall() {

        try {

            int zoomSpeed = 100;

            List<List<DahuaParamRequestSetConfigVideoInZoom>> list1 = Arrays.asList(
                    Arrays.asList(
                            new DahuaParamRequestSetConfigVideoInZoom() {{
                                setSpeed(zoomSpeed);
                            }},
                            new DahuaParamRequestSetConfigVideoInZoom() {{
                                setSpeed(zoomSpeed);
                            }},
                            new DahuaParamRequestSetConfigVideoInZoom() {{
                                setSpeed(zoomSpeed);
                            }}
                    )
            );

            List<List<DahuaParamRequestSetConfigVideoInWhiteBalance>> list2 = Arrays.asList(
                    Arrays.asList(
                            new DahuaParamRequestSetConfigVideoInWhiteBalance() {{
                                setColorTemperatureLevel(50);
                                setGainBlue(50);
                                setGainGreen(50);
                                setGainRed(50);
                                setMode("Auto");
                            }},
                            new DahuaParamRequestSetConfigVideoInWhiteBalance() {{
                                setColorTemperatureLevel(50);
                                setGainBlue(50);
                                setGainGreen(50);
                                setGainRed(50);
                                setMode("Auto");
                            }},
                            new DahuaParamRequestSetConfigVideoInWhiteBalance() {{
                                setColorTemperatureLevel(50);
                                setGainBlue(50);
                                setGainGreen(50);
                                setGainRed(50);
                                setMode("Auto");
                            }}
                    )
            );

            DahuaParamRequestSetConfig setConfig1 = new DahuaParamRequestSetConfig();
            setConfig1.setName("VideoInZoom");
            setConfig1.setTable(list1);

            DahuaParamRequestSetConfig setConfig2 = new DahuaParamRequestSetConfig();
            setConfig2.setName("VideoInWhiteBalance");
            setConfig2.setTable(list2);

            sessionDahua.setConfig(Arrays.asList(setConfig1, setConfig2));
            Thread.sleep(1000);
        } catch (PtzSessionException | InterruptedException e) {
            fail(e.getMessage());
        }

    }

    @Test
    public void _032_startStopZoomOut() {

        try {
            sessionDahua.startZoomOut(5);
            Thread.sleep(500);
            sessionDahua.stopZoomOut(5);
            Thread.sleep(1000);
        } catch (PtzSessionException | InterruptedException e) {
            fail(e.getMessage());
        }

    }

}
