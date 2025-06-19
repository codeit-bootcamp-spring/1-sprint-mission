package com.sprint.mission.discodeit.config;

import java.util.List;
import java.util.Optional;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;
import org.springframework.core.task.TaskExecutor;
import org.springframework.core.task.support.CompositeTaskDecorator;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
@EnableAsync
public class AsyncConfig {

  @Bean
  public TaskExecutor binaryContentTaskExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(10);
    executor.setMaxPoolSize(20);
    executor.setQueueCapacity(100);
    executor.setThreadNamePrefix("binary-content-");
    executor.setTaskDecorator(
        new CompositeTaskDecorator(List.of(mdcTaskDecorator(), securityContextTaskDecorator())));
    executor.initialize();
    return executor;
  }

  public TaskDecorator mdcTaskDecorator() {
    return runnable -> {
      Optional<String> requestId = Optional.ofNullable(MDC.get(MDCLoggingInterceptor.REQUEST_ID));
      return () -> {
        requestId.ifPresent(id -> MDC.put(MDCLoggingInterceptor.REQUEST_ID, id));
        try {
          runnable.run();
        } finally {
          requestId.ifPresent(id -> MDC.remove(MDCLoggingInterceptor.REQUEST_ID));
        }
      };
    };
  }

  public TaskDecorator securityContextTaskDecorator() {
    return runnable -> {
      SecurityContext securityContext = SecurityContextHolder.getContext();
      return () -> {
        SecurityContextHolder.setContext(securityContext);
        try {
          runnable.run();
        } finally {
          SecurityContextHolder.clearContext();
        }
      };
    };
  }
}
