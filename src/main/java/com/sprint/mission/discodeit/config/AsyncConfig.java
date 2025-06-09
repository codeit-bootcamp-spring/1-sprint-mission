package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.async.AsyncTaskDecorator;
import com.sprint.mission.discodeit.exception.AsyncExceptionHandler;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

  @Primary
  @Bean("defaultExecutor")
  public ThreadPoolTaskExecutor defaultExecutor(AsyncTaskDecorator decorator) {
    int core = Runtime.getRuntime().availableProcessors();

    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(core);
    executor.setMaxPoolSize(core * 2);
    executor.setQueueCapacity(1000);
    executor.setThreadNamePrefix("default-");
    executor.setTaskDecorator(decorator);
    executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
    executor.initialize();

    return executor;
  }

  /**
   * @methodName : filUploadExecutor
   * @date : 2025-06-04 오후 6:44
   * @author : wongil
   * @Description: 파일 업로드용 executor
   **/
  @Bean("fileUploadExecutor")
  public ThreadPoolTaskExecutor filUploadExecutor(AsyncTaskDecorator decorator) {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

    executor.setCorePoolSize(10);
    executor.setMaxPoolSize(30);
    executor.setQueueCapacity(2000);
    executor.setKeepAliveSeconds(60);
    executor.setThreadNamePrefix("upload-");
    executor.setTaskDecorator(decorator);
    executor.setAllowCoreThreadTimeOut(true);
    executor.setWaitForTasksToCompleteOnShutdown(true);
    executor.initialize();

    return executor;
  }

  @Bean("notificationExecutor")
  public ThreadPoolTaskExecutor notiExecutor(AsyncTaskDecorator decorator) {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

    executor.setCorePoolSize(10);
    executor.setMaxPoolSize(30);
    executor.setQueueCapacity(1000);
    executor.setTaskDecorator(decorator);
    executor.setKeepAliveSeconds(60);
    executor.setThreadNamePrefix("notification");
    executor.setAllowCoreThreadTimeOut(true);
    executor.setWaitForTasksToCompleteOnShutdown(true);
    executor.initialize();

    return executor;
  }

  @Override
  public Executor getAsyncExecutor() {
    return defaultExecutor(new AsyncTaskDecorator());
  }

  @Override
  public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
    return new AsyncExceptionHandler();
  }
}
