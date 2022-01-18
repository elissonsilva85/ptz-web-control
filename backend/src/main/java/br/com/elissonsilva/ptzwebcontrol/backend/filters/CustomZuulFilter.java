package br.com.elissonsilva.ptzwebcontrol.backend.filters;

import com.google.common.io.CharStreams;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.*;

import java.io.*;
import java.security.Principal;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.POST_TYPE;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.SEND_RESPONSE_FILTER_ORDER;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

public class CustomZuulFilter extends ZuulFilter {

    private static Logger log = LoggerFactory.getLogger(CustomZuulFilter.class);

    //@Override
    public String filterType() {
        log.info("filterType");
        return POST_TYPE;
    }

    //@Override
    public int filterOrder() {
        log.info("filterOrder");
        return SEND_RESPONSE_FILTER_ORDER + 1;
    }

    //@Override
    public boolean shouldFilter() {
        log.info("shouldFilter");
        return true;
    }

    //@Override
    public Object run() throws ZuulException {
        log.info("run");
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletResponse servletResponse = context.getResponse();
        HttpServletRequest servletRequest = context.getRequest();

        try (final InputStream responseDataStream = context.getResponseDataStream()) {

            if(responseDataStream == null) {
                log.info("BODY: {}", "");
                return null;
            }

            String responseData = CharStreams.toString(new InputStreamReader(responseDataStream, "UTF-8"));
            log.info("BODY: {}", responseData);

            context.setResponseBody(responseData);
        }
        catch (Exception e) {
            log.error(e.getMessage());
            return null;
            //throw new ZuulException(e, INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }

        log.info(String.format("%s request to %s", servletRequest.getMethod(), servletRequest.getRequestURL().toString()));
        return null;
    }

}

