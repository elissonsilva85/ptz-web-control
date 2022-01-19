package br.com.elissonsilva.ptzwebcontrol.backend.filters;

import com.google.common.io.CharStreams;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.FORM_BODY_WRAPPER_FILTER_ORDER;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.POST_TYPE;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

public class PostZuulFilter extends ZuulFilter {

    private static Logger log = LoggerFactory.getLogger(PostZuulFilter.class);

    @Override
    public String filterType() {
        return POST_TYPE;
    }

    @Override
    public int filterOrder() {
        return FORM_BODY_WRAPPER_FILTER_ORDER;
    }

    //@Override
    public boolean shouldFilter() {
        return true;
    }

    //@Override
    @SneakyThrows
    public Object run() throws ZuulException {
        log.info("run");
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest servletRequest = context.getRequest();
        log.info("RequestURL: " + servletRequest.getRequestURL());

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
            log.error(e.getMessage(), e);
            throw new ZuulException(e, INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }

        log.info(String.format("%s request to %s", servletRequest.getMethod(), servletRequest.getRequestURL().toString()));
        return null;
    }

}

