package com.sprint.mission.discodeit.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class MDCLoggingInterceptor implements HandlerInterceptor {

  private static final String REQUEST_ID = "requestId";
  private static final String REQUEST_METHOD = "requestMethod";
  private static final String REQUEST_URI = "requestURI";
  private static final String RESPONSE_HEADER_NAME = "Discodeit-Request-ID";

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
      Object handler) {
    String requestId = UUID.randomUUID().toString();
    MDC.put(REQUEST_ID, requestId);
    MDC.put(REQUEST_METHOD, request.getMethod());
    MDC.put(REQUEST_URI, request.getRequestURI());

    response.addHeader(RESPONSE_HEADER_NAME, requestId);

    return true;
  }

  @Override
  public void afterCompletion(HttpServletRequest request,
      HttpServletResponse response,
      Object handler,
      Exception ex) {
    // MDC 정리
    MDC.clear();
  }
}
