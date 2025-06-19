package com.sprint.mission.discodeit.configure;

import java.io.Serial;
import java.util.Map;
import org.slf4j.MDC;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
public class MdcTaskExecutor extends ThreadPoolTaskExecutor {

  @Serial
  private static final long serialVersionUID = -5702079831205296680L;

  @Override
  public void execute(Runnable task) {
    Map<String, String> contextMap = MDC.getCopyOfContextMap();
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    super.execute(() -> {
      try {
        MDC.setContextMap(contextMap);
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);
        task.run();
      } finally {
        MDC.clear();
        SecurityContextHolder.clearContext();
      }
    });
  }
}
