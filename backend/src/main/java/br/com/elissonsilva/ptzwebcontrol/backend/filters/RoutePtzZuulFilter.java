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

public class RoutePtzZuulFilter extends ZuulFilter {

    private static Logger log = LoggerFactory.getLogger(RoutePtzZuulFilter.class);

    @Autowired
    private Config config;

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
        return RequestContext.getCurrentContext().getRequest().getRequestURI().startsWith("/ptz");
    }

    //@Override
    @SneakyThrows
    public Object run() throws ZuulException {
        log.debug("run");
        RequestContext ctx = RequestContext.getCurrentContext();

        //
        String ptzCode = "";
        String requestURI = ctx.getRequest().getRequestURI();
        String requestURIRegex = "^\\/(ptz)\\/(.+)\\/(.+)";
        Pattern pattern = Pattern.compile(requestURIRegex);
        Matcher matcher = pattern.matcher(requestURI);
        if (matcher.matches()) {
            ptzCode = matcher.group(2);
        }

        requestURI = requestURI.replaceFirst("^\\/(ptz)\\/(.+)\\/", "/");
        ctx.set(REQUEST_URI_KEY, requestURI );

        //
        String routeUrl = config.getPtz().getConnection().get(ptzCode).getUrl();

        //
        ctx.setRouteHost(new URL(routeUrl));

        //
        log.debug("[" + ptzCode + "] RouteHost ..: " + ctx.getRouteHost());
        log.debug("[" + ptzCode + "] RequestURI .: " + requestURI);

        return null;
    }

}

