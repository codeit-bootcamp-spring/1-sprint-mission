package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.async.AsyncTaskDecorator;
import java.util.Map;
import java.util.concurrent.Executor;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.concurrent.DelegatingSecurityContextExecutor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
@EnableAsync
@EnableRetry
public class AsyncConfig {

  @Bean
  public Executor asyncExecutor() {
    int core = Runtime.getRuntime().availableProcessors();
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(core);
    executor.setMaxPoolSize(core * 2);
    executor.setQueueCapacity(100);
    executor.setThreadNamePrefix("AsyncExecutor-");

    executor.setTaskDecorator(new AsyncTaskDecorator());

    executor.initialize();
    return executor;
  }
}