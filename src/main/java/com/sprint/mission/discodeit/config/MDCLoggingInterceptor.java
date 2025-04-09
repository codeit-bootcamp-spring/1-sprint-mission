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

  private static final String REQUEST_ID_HEADER = "Discodeit-Request-Id";

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {

    String requestId = UUID.randomUUID().toString().substring(0, 8);
    String method = request.getMethod();
    String uri = request.getRequestURI();

    MDC.put("requestId", requestId);
    MDC.put("method", method);
    MDC.put("uri", uri);

    response.setHeader(REQUEST_ID_HEADER, requestId);

    log.info("Incoming request [{}] [{}] [{}]", requestId, method, uri);
    return true;
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
      Object handler, Exception ex) throws Exception {
    log.info("Completed with status [{}]", response.getStatus());
    MDC.clear();
  }
}
