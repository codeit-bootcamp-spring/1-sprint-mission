package com.sprint.mission.discodeit.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * This log is for development only
 */
@Slf4j
@Component
public class RequestLoggingInterceptor implements HandlerInterceptor {

  private static final String REQUEST_ID = "requestId";
  private static final String REQUEST_METHOD = "requestMethod";
  private static final String REQUEST_URI = "requestUri";
  private static final String RESPONSE_HEADER_NAME = "Discodeit-Request-ID";

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {

    String uuid = UUID.randomUUID().toString();
    MDC.put(REQUEST_ID, uuid);
    MDC.put(REQUEST_METHOD, request.getMethod());
    MDC.put(REQUEST_URI, request.getRequestURI());

    log.info("[API Request] : [{}][{}]", request.getMethod(), request.getRequestURI());
    return true;
  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
      ModelAndView modelAndView) throws Exception {
    String reqId = MDC.get(REQUEST_ID);

    if (reqId != null) {
      response.setHeader(RESPONSE_HEADER_NAME, reqId);
    }
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
      Object handler, Exception ex) throws Exception {

    log.info("[API Response] : [{}][{}] | [Status Code : {}]", request.getMethod(),
        request.getRequestURI(), response.getStatus());
    if (ex != null) {
      log.info("[Error] : [{}] : [{}]", ex, ex.getMessage());
    }

    MDC.clear();
  }
}
