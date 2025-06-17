package com.sprint.mission.discodeit.config.mdc;

import java.util.Map;
import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class MdcTaskDecorator implements TaskDecorator {
  @Override
  public Runnable decorate(Runnable runnable) {
    SecurityContext securityContext = SecurityContextHolder.getContext();
    Map<String, String> contextMap = MDC.getCopyOfContextMap();
    return () -> {
      try {
        //새로운 쓰레드에 넘겨질 수 있도록
        SecurityContextHolder.setContext(securityContext);
        if (contextMap != null) MDC.setContextMap(contextMap);
        runnable.run();
      } finally {
        SecurityContextHolder.clearContext();
        MDC.clear();
      }
    };
  }
}
