package com.sprint.mission.discodeit.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;
import org.jboss.logging.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class MDCLoggingInterceptor implements HandlerInterceptor {

  private static final String REQUEST_ID_KEY = "requestId";
  private static final String METHOD_KEY = "httpMethod";
  private static final String URI_KEY = "requestURI";
  private static final String RESPONSE_HEADER = "Discodeit-Request-ID";

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
      Object handler) {
    String requestId = UUID.randomUUID().toString();
    String method = request.getMethod();
    String uri = request.getRequestURI();

    MDC.put(REQUEST_ID_KEY, requestId);
    MDC.put(METHOD_KEY, method);
    MDC.put(URI_KEY, uri);

    response.setHeader(RESPONSE_HEADER, requestId);

    return true;
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
      Object handler, Exception ex) {
    MDC.clear();
  }
}