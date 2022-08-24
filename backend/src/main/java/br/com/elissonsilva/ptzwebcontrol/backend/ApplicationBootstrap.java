package br.com.elissonsilva.ptzwebcontrol.backend;

import br.com.elissonsilva.ptzwebcontrol.backend.component.Configuration;
import br.com.elissonsilva.ptzwebcontrol.backend.component.EnvUtil;
import br.com.elissonsilva.ptzwebcontrol.backend.filters.OkHttpRoutingFilter;
import br.com.elissonsilva.ptzwebcontrol.backend.filters.RoutePtzZuulFilter;
import br.com.elissonsilva.ptzwebcontrol.backend.filters.RouteVmixZuulFilter;
import br.com.elissonsilva.ptzwebcontrol.backend.services.PtzSessionManagerService;
import br.com.elissonsilva.ptzwebcontrol.backend.services.UDPServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

@EnableZuulProxy
@SpringBootApplication
public class ApplicationBootstrap extends SpringBootServletInitializer {

    @Autowired
    private EnvUtil envUtil;

    @Autowired
    private Configuration configuration;

    @Autowired
    private PtzSessionManagerService ptzSessionManagerService;

    private Map<String, UDPServerService> udpThreads;

    public static void main(String[] args) {
        SpringApplication.run(ApplicationBootstrap.class, args);
    }

    @PostConstruct
    private void postConstruct() {
        try {
            if(configuration.getUrlBase() == null) {
                String host = envUtil.getHostname();
                String port = envUtil.getPort();
                if (!"80".equals(port)) host += ":" + port;
                host = "http://" + host + "/";
                configuration.setUrlBase(host);
            }
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public RoutePtzZuulFilter routePtzZuulFilter() {
        return new RoutePtzZuulFilter();
    }

    @Bean
    public RouteVmixZuulFilter routeVmixZuulFilter() {
        return new RouteVmixZuulFilter();
    }

    @Bean
    public OkHttpRoutingFilter okHttpRoutingFilter() { return new OkHttpRoutingFilter(); }

    @Bean
    public void processUniCastUdpMessage() {
        udpThreads = new HashMap<>();
        if(configuration.getPtz() != null)
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
