package br.com.elissonsilva.ptzwebcontrol.backend.filters;

import br.com.elissonsilva.ptzwebcontrol.backend.component.Configuration;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.*;

public class RouteVmixZuulFilter extends ZuulFilter {

    @Autowired
    private Configuration configuration;

    private final static Logger log = LoggerFactory.getLogger(RouteVmixZuulFilter.class);

    @Override
    public String filterType() {
        return ROUTE_TYPE;
    }

    @Override
    public int filterOrder() {
        return PRE_DECORATION_FILTER_ORDER - 1;
    }

    //@Override
    public boolean shouldFilter() {
        return RequestContext.getCurrentContext().getRequest().getRequestURI().startsWith("/vmix");
    }

    //@Override
    @SneakyThrows
    public Object run() {
        log.debug("run");
        RequestContext ctx = RequestContext.getCurrentContext();

        //
        String requestURI = ctx.getRequest().getRequestURI().replaceFirst("^\\/(vmix)\\/", "/");
        ctx.set(REQUEST_URI_KEY, requestURI );

        //
        String routeUrl = configuration.getVmixUrl();

        //
        ctx.setRouteHost(new URL(routeUrl));

        //
        log.debug("RouteHost ..: " + ctx.getRouteHost());
        log.debug("RequestURI .: " + requestURI);

        return null;
    }

}

