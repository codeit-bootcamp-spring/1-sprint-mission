package com.sprint.mission.discodeit.common;

import java.util.Map;
import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class ContextCopyingTaskDecorator implements TaskDecorator {

  @Override
  public Runnable decorate(Runnable runnable) {
    Map<String, String> mdcContext = MDC.getCopyOfContextMap();
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    return () -> {
      if (mdcContext != null) {
        MDC.setContextMap(mdcContext);
      }
      try {
        SecurityContextHolder.getContext().setAuthentication(authentication);
        runnable.run();
      } finally {
        MDC.clear();
        SecurityContextHolder.clearContext();
      }
    };
  }
}
