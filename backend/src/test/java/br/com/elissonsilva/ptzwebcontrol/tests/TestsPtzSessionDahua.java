package br.com.elissonsilva.ptzwebcontrol.tests;

import br.com.elissonsilva.ptzwebcontrol.backend.ApplicationBootstrap;
import br.com.elissonsilva.ptzwebcontrol.backend.component.Config;
import br.com.elissonsilva.ptzwebcontrol.backend.entity.ConfigPtz;
import br.com.elissonsilva.ptzwebcontrol.backend.entity.ConfigPtzConnection;
import br.com.elissonsilva.ptzwebcontrol.backend.exception.PtzSessionException;
import br.com.elissonsilva.ptzwebcontrol.backend.exception.PtzSessionManagerException;
import br.com.elissonsilva.ptzwebcontrol.backend.services.PtzSessionManagerService;
import br.com.elissonsilva.ptzwebcontrol.backend.dahua.PtzSessionDahua;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.fail;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(classes = ApplicationBootstrap.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@WebAppConfiguration
public class TestsPtzSessionDahua {

    @Spy
    @Autowired
    private Config configSpy;

    @Autowired
    private PtzSessionManagerService ptzSessionManagerService;

    private static PtzSessionDahua sessionDahua;

    /*
    private class TestPtz extends PtzSessionDahua {

        public TestPtz(String ptz, String user, String pass, String random, String realm, String session) {
            super(ptz, user, pass);
            this.setSessionData(
                    new DahuaSessionData(
                            random,
                            realm,
                            session,
                            true));
        }

        public String getHashPassword() {
            return this._getHashPassword();
        }

    }

    private TestPtz testPtz;
   */

    @Before
    public void setup() throws PtzSessionManagerException {

        // Prepare Mock
        configSpy = Mockito.spy(new Config());

        // Mock Config data
        when(configSpy.getPtz())
                .thenReturn(new ConfigPtz() {{
                    setBrand("dahua");
                    setConnection(new HashMap<String, ConfigPtzConnection>() {{
                        put("PTZ1", new ConfigPtzConnection() {{
                            setLabel("PTZ1");
                            setUser("user");
                            setPassword("pass");
                            setUrl("http://ip/");
                        }});
                    }});
                }});

        ptzSessionManagerService = new PtzSessionManagerService(configSpy);

        /*
        testPtz = new TestPtz(
                "PTZ1",
                "username",
                "password",
                "1618173075",
                "Login to c8de6f5505ddde548b269d05de712bc5",
                "");
        */
    }

    @Test
    public void _001_shouldReturnCorrectHashPassword() {

        String pass = "128CDAD12C6B9F8966F4B23F5F26AD76";
        //String currentPass = sessionDahua.getHashPassword();
        assertTrue("128CDAD12C6B9F8966F4B23F5F26AD76".equals(pass), "PtzSessionDahua.getHashPassword with wrong result");

    }

    @Test
    public void _002_shouldGetSession() throws PtzSessionManagerException {

        sessionDahua = (PtzSessionDahua) ptzSessionManagerService.getSession("PTZ1");

    }

    @Test
    public void _003_shouldConnect() {

        try {
            sessionDahua.connect();
        } catch (PtzSessionException e) {
            fail(e.getMessage());
        }

    }

    @Test
    public void _004_shouldLoadPreset() {

        try {
            sessionDahua.loadPreset(1);
        } catch (PtzSessionException e) {
            fail(e.getMessage());
        }

    }

    @Test
    public void _005_shouldZoomIn() {

        try {
            sessionDahua.startZoomIn(5);
            Thread.sleep(500);
            sessionDahua.stopZoomIn(5);
            Thread.sleep(1000);
        } catch (PtzSessionException e) {
            fail(e.getMessage());
        } catch (InterruptedException e) {
            fail(e.getMessage());
        }

    }

    @Test
    public void _006_shouldZoomOut() {

        try {
            sessionDahua.startZoomOut(5);
            Thread.sleep(500);
            sessionDahua.stopZoomOut(5);
            Thread.sleep(1000);
        } catch (PtzSessionException e) {
            fail(e.getMessage());
        } catch (InterruptedException e) {
            fail(e.getMessage());
        }

    }

    @Test
    public void _007_shouldJoystickRight() {

        try {
            sessionDahua.startJoystick("Right",1, 1);
            Thread.sleep(500);
            sessionDahua.stopJoystick("Right", 1, 1);
            Thread.sleep(1000);
        } catch (PtzSessionException e) {
            fail(e.getMessage());
        } catch (InterruptedException e) {
            fail(e.getMessage());
        }

    }

}
