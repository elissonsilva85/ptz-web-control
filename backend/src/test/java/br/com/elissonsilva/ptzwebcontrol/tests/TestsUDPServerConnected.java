package br.com.elissonsilva.ptzwebcontrol.tests;

import br.com.elissonsilva.ptzwebcontrol.backend.ApplicationBootstrap;
import br.com.elissonsilva.ptzwebcontrol.backend.component.Configuration;
import br.com.elissonsilva.ptzwebcontrol.backend.ptz.dahua.PtzSessionDahua;
import br.com.elissonsilva.ptzwebcontrol.backend.services.PtzSessionManagerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
// Removed RunWith for JUnit 5
// Removed runners import for JUnit 5 MethodSorters;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

// Removed RunWith annotation for JUnit 5
@SpringBootTest(classes = ApplicationBootstrap.class)
@TestMethodOrder(org.junit.jupiter.api.MethodOrderer.OrderAnnotation.class)
@WebAppConfiguration
@Disabled
public class TestsUDPServerConnected {

    @Spy
    @Autowired
    private Configuration configurationSpy;

    @Autowired
    private PtzSessionManagerService ptzSessionManagerService;

    private static PtzSessionDahua sessionDahua;

    @BeforeEach
    public void setup() {

    }

    @Test
    public void _001_shouldSendPackage() {



    }
}
