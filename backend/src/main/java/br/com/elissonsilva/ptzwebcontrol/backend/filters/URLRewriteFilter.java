package br.com.elissonsilva.ptzwebcontrol.backend.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class URLRewriteFilter implements Filter {

    private static Logger log = LoggerFactory.getLogger(URLRewriteFilter.class);

    private final String API_PATTERN = "^\\/(api|ptz|vmix)\\/(.*)$";
    private final String POINT_EXCLUSION_PATTERN = "^([^.]+)$";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        String requestURI = servletRequest.getRequestURI();
        String contextPath = servletRequest.getContextPath();
        if(!requestURI.equals(contextPath) &&
                !requestURI.matches(API_PATTERN) && // Check if the requested URL is not a controller (/api/**)
                    requestURI.matches(POINT_EXCLUSION_PATTERN) // Check if there are no "." in requested URL
        ) {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/");
            dispatcher.forward(request, response);
            return;
        }
        chain.doFilter(request, response);
    }

}
