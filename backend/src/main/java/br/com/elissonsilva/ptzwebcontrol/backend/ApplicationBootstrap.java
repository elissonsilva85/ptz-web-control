package br.com.elissonsilva.ptzwebcontrol.backend;

import br.com.elissonsilva.ptzwebcontrol.backend.component.Configuration;
import br.com.elissonsilva.ptzwebcontrol.backend.services.PtzSessionManagerService;
import br.com.elissonsilva.ptzwebcontrol.backend.services.UDPServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class ApplicationBootstrap extends SpringBootServletInitializer {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationBootstrap.class);

    @Autowired
    private Configuration configuration;

    @Autowired
    private PtzSessionManagerService ptzSessionManagerService;

    private Map<String, UDPServerService> udpThreads;

    public static void main(String[] args) {
        SpringApplication.run(ApplicationBootstrap.class, args);
    }



    @Bean
    public void processUniCastUdpMessage() {
        udpThreads = new HashMap<>();
        configuration.getPtz().getConnection().forEach((ptz, conn) -> {
            try {
                udpThreads.put(ptz,new UDPServerService(ptzSessionManagerService, ptz,conn.getUdpPort()));
                udpThreads.get(ptz).start();
            } catch (SocketException e) {
                logger.warn(ptz + " processUniCastUdpMessage: " + e.getMessage(), e);
            }
        });
    }

    @PreDestroy
    public void onExit() {
        udpThreads.forEach((ptz, thread) -> {
            thread.close();
            thread.interrupt();
        });
    }

}
