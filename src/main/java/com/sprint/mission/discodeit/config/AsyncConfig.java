package com.sprint.mission.discodeit.config;

import java.util.concurrent.Executor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

  @Override
  public Executor getAsyncExecutor() {
    int cores = Runtime.getRuntime().availableProcessors();

    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(cores);
    executor.setMaxPoolSize(cores * 2);
    executor.setQueueCapacity(1000);
    executor.setThreadNamePrefix("AsyncExecutor-");
    executor.setTaskDecorator(new MdcTaskDecorator());
    executor.initialize();
    return executor;
  }
}
