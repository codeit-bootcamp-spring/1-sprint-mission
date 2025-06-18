package com.sprint.mission.discodeit.decorator;


import java.util.Map;
import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class MdcTaskDecorator implements TaskDecorator {

  @Override
  public Runnable decorate(Runnable runnable) {
    SecurityContext securityContext = SecurityContextHolder.getContext();
    Map<String, String> contextMap = MDC.getCopyOfContextMap();
    return () -> {
      try {
        SecurityContextHolder.setContext(securityContext);
        if (contextMap != null) {
          MDC.setContextMap(contextMap);
        }

        runnable.run();
      } finally {
        SecurityContextHolder.clearContext();
        MDC.clear();
      }
    };
  }
}
