package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.common.ContextCopyingTaskDecorator;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

  @Bean("unifiedPool")
  @Primary // 같은 타입의 Bean 중 우선순위 부여
  public ThreadPoolTaskExecutor unifiedPool() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(4);
    executor.setMaxPoolSize(10);
    executor.setQueueCapacity(500);
    executor.setThreadNamePrefix("unified-");

    executor.setTaskDecorator(new ContextCopyingTaskDecorator());

    executor.setRejectedExecutionHandler(new CallerRunsPolicy());

    executor.setWaitForTasksToCompleteOnShutdown(true);
    executor.setAwaitTerminationSeconds(30);
    executor.initialize();

    return executor;
  }

  @Bean(name = "notificationPool")
  public Executor notificationPool() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(10);
    executor.setMaxPoolSize(20);
    executor.setQueueCapacity(100);
    executor.setThreadNamePrefix("Notification-");
    executor.setTaskDecorator(new ContextCopyingTaskDecorator());

    executor.setWaitForTasksToCompleteOnShutdown(true);
    executor.setRejectedExecutionHandler(new CallerRunsPolicy());

    executor.initialize();
    return executor;
  }

  @Override
  public Executor getAsyncExecutor() {
    return unifiedPool();
  }

  @Bean
  public ExecutorService executorService(
      @Qualifier("unifiedPool") ThreadPoolTaskExecutor taskExecutor) {
    return taskExecutor.getThreadPoolExecutor();
  }
}
