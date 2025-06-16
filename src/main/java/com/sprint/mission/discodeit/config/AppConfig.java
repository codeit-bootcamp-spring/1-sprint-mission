package com.sprint.mission.discodeit.config;

import java.util.Map;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.TaskDecorator;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;

@Configuration
@EnableJpaAuditing
@EnableAsync
@EnableRetry
public class AppConfig {

  @Bean(name = "binaryContentExecutor")
  public AsyncTaskExecutor asyncExecutor() {
    ThreadPoolTaskExecutor delegate = new ThreadPoolTaskExecutor();
    delegate.setCorePoolSize(4);
    delegate.setMaxPoolSize(16);
    delegate.setQueueCapacity(100);
    delegate.setThreadNamePrefix("binaryContent-");
    delegate.setTaskDecorator(new MdcTaskDecorator());
    delegate.initialize();
    return new DelegatingSecurityContextAsyncTaskExecutor(delegate);
  }

  private static class MdcTaskDecorator implements TaskDecorator {
    @Override
    public Runnable decorate(Runnable runnable) {
      Map<String, String> contextMap = MDC.getCopyOfContextMap();

      return () -> {
        Map<String, String> previous = MDC.getCopyOfContextMap();
        try {
          if (contextMap != null) {
            MDC.setContextMap(contextMap);
          }
          runnable.run();
        } finally {
          MDC.clear();
          if (previous != null) MDC.setContextMap(previous);
        }
      };
    }
  }

}
