package com.sprint.mission.discodeit.config;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@RequiredArgsConstructor
@EnableAsync // 비동기 기능 활성
@EnableRetry // 재시도 기능 활성
public class AsyncConfig implements AsyncConfigurer {

  private final ContextPropagatingTaskDecorator decorator;


  @Bean("defaultExecutor")
  public ThreadPoolTaskExecutor defaultExecutor() {

    int cores = Runtime.getRuntime().availableProcessors();

    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(cores); // 기본 스레드 수
    executor.setMaxPoolSize(cores * 2); // 최대 스레드 수
    executor.setQueueCapacity(1000); // 대기열
    executor.setThreadNamePrefix("default-");
    executor.setTaskDecorator(decorator); // 스레드 context 유지
    executor.setRejectedExecutionHandler(
        new ThreadPoolExecutor.CallerRunsPolicy()
    ); // 거부 정책
    executor.initialize();
    return executor;
  }

  // Spring 의 기본 Executor 설정
  @Override
  public Executor getAsyncExecutor() {
    return defaultExecutor();
  }

  // 반환갑 void 인 건 확인해보고 추가


}
