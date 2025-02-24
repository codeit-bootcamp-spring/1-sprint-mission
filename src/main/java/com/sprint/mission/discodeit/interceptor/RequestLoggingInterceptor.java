package com.sprint.mission.discodeit.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
/**
 * This log is for development only
 */
@Slf4j
@Component
public class RequestLoggingInterceptor implements HandlerInterceptor {

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    log.info("[API Request] : [{}][{}]", request.getMethod(), request.getRequestURI());
    return true;
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    log.info("[API Response] : [{}][{}] | [Status Code : {}]", request.getMethod(), request.getRequestURI(), response.getStatus());
    if(ex != null){
      log.info("[Error] : [{}] : [{}]", ex, ex.getMessage());
    }
  }
}
