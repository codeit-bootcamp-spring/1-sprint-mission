package com.sprint.mission.discodeit.async;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

@Slf4j
@Component
public class AsyncTaskDecorator implements TaskDecorator {

  @Override
  public Runnable decorate(Runnable runnable) {
    Map<String, String> mdcContext = MDC.getCopyOfContextMap();
    SecurityContext securityContext = SecurityContextHolder.getContext();
    RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

    return () -> {
      Map<String, String> preMdcContext = MDC.getCopyOfContextMap();
      SecurityContext preSecurityContext = SecurityContextHolder.getContext();
      RequestAttributes preRequestAttribute = RequestContextHolder.getRequestAttributes();

      try {
        if (mdcContext != null) {
          MDC.setContextMap(mdcContext);
        }
        SecurityContextHolder.setContext(securityContext);
        RequestContextHolder.setRequestAttributes(requestAttributes, true);

        runnable.run();

      } finally {
        if (preMdcContext != null) {
          MDC.setContextMap(preMdcContext);
        } else {
          MDC.clear();
        }

        SecurityContextHolder.setContext(preSecurityContext);
        RequestContextHolder.setRequestAttributes(preRequestAttribute, true);
      }
    };
  }
}
