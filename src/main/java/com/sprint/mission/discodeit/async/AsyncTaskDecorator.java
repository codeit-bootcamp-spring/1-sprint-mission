package com.sprint.mission.discodeit.async;

import java.util.Map;
import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

@Component
public class AsyncTaskDecorator implements TaskDecorator {

  @Override
  public Runnable decorate(Runnable task) {
    Map<String, String> contextMap = MDC.getCopyOfContextMap();
    SecurityContext securityContext = SecurityContextHolder.getContext();
    RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

    return () -> {
      Map<String, String> preContextMap = MDC.getCopyOfContextMap();
      SecurityContext preSecurityContext = SecurityContextHolder.getContext();
      RequestAttributes preRequestAttributes = RequestContextHolder.getRequestAttributes();

      try {
        if (contextMap != null) {
          MDC.setContextMap(contextMap);
        } else {
          MDC.clear();
        }

        SecurityContextHolder.setContext(securityContext);
        RequestContextHolder.setRequestAttributes(requestAttributes, true);

        task.run();

      } finally {
        if (preContextMap != null) {
          MDC.setContextMap(preContextMap);
        } else {
          MDC.clear();
        }

        SecurityContextHolder.setContext(preSecurityContext);
        RequestContextHolder.setRequestAttributes(preRequestAttributes, true);
      }
    };
  }
}