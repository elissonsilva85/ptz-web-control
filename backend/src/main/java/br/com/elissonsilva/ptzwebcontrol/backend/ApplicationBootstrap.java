package br.com.elissonsilva.ptzwebcontrol.backend;

import br.com.elissonsilva.ptzwebcontrol.backend.filters.RoutePtzZuulFilter;
import br.com.elissonsilva.ptzwebcontrol.backend.filters.RouteVmixZuulFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

@EnableZuulProxy
@SpringBootApplication
public class ApplicationBootstrap extends SpringBootServletInitializer {

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

    //@Bean
    //public PostZuulFilter postZuulFilter() {
    //    return new PostZuulFilter();
    //}
}
