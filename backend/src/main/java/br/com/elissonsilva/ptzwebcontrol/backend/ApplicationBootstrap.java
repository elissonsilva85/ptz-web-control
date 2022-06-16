package br.com.elissonsilva.ptzwebcontrol.backend;

import br.com.elissonsilva.ptzwebcontrol.backend.component.Config;
import br.com.elissonsilva.ptzwebcontrol.backend.exception.PtzSessionManagerException;
import br.com.elissonsilva.ptzwebcontrol.backend.filters.OkHttpRoutingFilter;
import br.com.elissonsilva.ptzwebcontrol.backend.filters.RoutePtzZuulFilter;
import br.com.elissonsilva.ptzwebcontrol.backend.filters.RouteVmixZuulFilter;
import br.com.elissonsilva.ptzwebcontrol.backend.services.UDPServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.context.IntegrationFlowContext;
import org.springframework.integration.ip.udp.UnicastReceivingChannelAdapter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@EnableZuulProxy
@SpringBootApplication
public class ApplicationBootstrap extends SpringBootServletInitializer {

    @Autowired
    private Config config;

    @Autowired
    private IntegrationFlowContext flowContext;

    private final Map<Integer, IntegrationFlowContext.IntegrationFlowRegistration> registrations = new HashMap<>();

    public static void main(String[] args) {
        SpringApplication.run(ApplicationBootstrap.class, args);
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

    public UnicastReceivingChannelAdapter makeANewUdpInbound(String ptz, int port) {
        System.out.println("Creating an adapter to receive from port " + port);
        UnicastReceivingChannelAdapter source = new UnicastReceivingChannelAdapter(port);
        IntegrationFlow flow = IntegrationFlows.from(source)
                .enrichHeaders(Collections.singletonMap("destPTZ", ptz))
                .handle("UDPServerService", "handleMessage")
                .get();
        IntegrationFlowContext.IntegrationFlowRegistration registration = flowContext.registration(flow).register();
        registrations.put(port, registration);
        return source;
    }

    @Bean
    public void processUniCastUdpMessage() {
        config.getPtz().getConnection().forEach((ptz, conn) -> {
            makeANewUdpInbound(ptz,conn.getUdpPort());
        });
    }

}
