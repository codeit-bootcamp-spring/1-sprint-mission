package com.sprint.mission.discodeit.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jboss.logging.MDC;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

@Slf4j
public class MDCLoggingInterceptor implements HandlerInterceptor {

    public static final String REQUEST_ID = "requestId";
    public static final String REQUEST_METHOD = "requestMethod";
    public static final String REQUEST_URI = "requestUri";

    public static final String REQUEST_ID_HEADER = "Discodeit-Request-ID";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        String requestId = UUID.randomUUID().toString().replaceAll("-", "");

        MDC.put(REQUEST_ID, requestId);
        MDC.put(REQUEST_METHOD, request.getMethod());
        MDC.put(REQUEST_URI, request.getRequestURI());

        response.setHeader(REQUEST_ID_HEADER, requestId);

        log.debug("Requst started");
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

        log.debug("Request completed");
        MDC.clear();
    }
}
