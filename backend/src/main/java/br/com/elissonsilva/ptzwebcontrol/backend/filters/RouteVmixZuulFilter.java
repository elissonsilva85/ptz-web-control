package br.com.elissonsilva.ptzwebcontrol.backend.filters;

import br.com.elissonsilva.ptzwebcontrol.backend.component.Config;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.*;

public class RouteVmixZuulFilter extends ZuulFilter {

    @Autowired
    private Config config;

    private static Logger log = LoggerFactory.getLogger(RouteVmixZuulFilter.class);

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
    public Object run() throws ZuulException {
        log.info("run");
        RequestContext ctx = RequestContext.getCurrentContext();

        //
        String requestURI = ctx.getRequest().getRequestURI().replaceFirst("^\\/(vmix)\\/", "/");
        ctx.set(REQUEST_URI_KEY, requestURI );

        //
        String routeUrl = config.getVmixUrl();

        //
        ctx.setRouteHost(new URL(routeUrl));

        //
        log.info("RouteHost ..: " + ctx.getRouteHost());
        log.info("RequestURI .: " + requestURI);

        return null;
    }

}

