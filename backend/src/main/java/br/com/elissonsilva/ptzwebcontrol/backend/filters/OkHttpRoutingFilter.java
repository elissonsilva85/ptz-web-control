package br.com.elissonsilva.ptzwebcontrol.backend.filters;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import okhttp3.*;
import okhttp3.internal.http.HttpMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.ProxyRequestHelper;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.StreamUtils;

import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.ROUTE_TYPE;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.SIMPLE_HOST_ROUTING_FILTER_ORDER;

public class OkHttpRoutingFilter extends ZuulFilter {

    private static Logger log = LoggerFactory.getLogger(OkHttpRoutingFilter.class);

    @Autowired
    private ProxyRequestHelper helper;

    @Override
    public String filterType() {
        return ROUTE_TYPE;
    }

    @Override
    public int filterOrder() {
        return SIMPLE_HOST_ROUTING_FILTER_ORDER - 1;
    }

    @Override
    public boolean shouldFilter() {
        return RequestContext.getCurrentContext().getRouteHost() != null
                && RequestContext.getCurrentContext().sendZuulResponse();
    }

    @Override
    public Object run() {
        RequestContext context = RequestContext.getCurrentContext();
        try {
            OkHttpClient httpClient = new OkHttpClient.Builder()
                    .build();

            HttpServletRequest request = context.getRequest();
            String method = request.getMethod();
            String uri = this.helper.buildZuulRequestURI(request);
            String queryParam = request.getQueryString();
            String url = context.getRouteHost() + uri + (queryParam != null ? "?" + queryParam : "");

            log.debug("--- HEADERS ---------------------------");
            Headers.Builder headers = new Headers.Builder();
            Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String name = headerNames.nextElement();
                Enumeration<String> values = request.getHeaders(name);

                while (values.hasMoreElements()) {
                    String value = values.nextElement();
                    headers.add(name, value);
                    log.debug(name + ": " + value);
                }
            }
            log.debug("---------------------------------------");

            InputStream inputStream = request.getInputStream();
            String postBody = new String(StreamUtils.copyToByteArray(inputStream), StandardCharsets.UTF_8);
            postBody = URLDecoder.decode(postBody, StandardCharsets.UTF_8);

            RequestBody requestBody = null;
            if (inputStream != null && HttpMethod.permitsRequestBody(method)) {
                MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
                requestBody = RequestBody.create(mediaType, postBody);
            }

            Request.Builder builder = new Request.Builder()
                    .headers(headers.build())
                    .url(url)
                    .method(method, requestBody);

            Response response = httpClient.newCall(builder.build()).execute();

            LinkedMultiValueMap<String, String> responseHeaders = new LinkedMultiValueMap<>();

            for (Map.Entry<String, List<String>> entry : response.headers().toMultimap().entrySet()) {
                responseHeaders.put(entry.getKey(), entry.getValue());
            }

            this.helper.setResponse(response.code(), response.body().byteStream(),
                    responseHeaders);
        } catch (IOException e) {
            e.printStackTrace();
        }
        context.setRouteHost(null); // prevent SimpleHostRoutingFilter from running
        return null;
    }
}
