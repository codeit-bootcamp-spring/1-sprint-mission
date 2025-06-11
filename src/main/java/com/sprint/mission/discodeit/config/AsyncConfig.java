package com.sprint.mission.discodeit.config;


import java.util.concurrent.ThreadPoolExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.sprint.mission.discodeit.context.ContextPropagatingTaskDecorator;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {

  // 기본적으로 ThreadPoolTaskExecutor 사용한다.(추가 설정 없어도 됨 - 단, 최적화는 안된 상태!)

  //ThreadPoolTaskExecutor: 스레드 풀 관리 클래스
  // - 여러 개의 스레드를 미리 만들어 놓고 작업을 배분하는 역할을 한다.
  // - 기본 설정:
  //    corePoolSize(항상 유지되는 스레드 수)  / maxPoolSize(최대 스레드 수) / queueCapacity(대기열 크기)

  //Brian Goetz의 공식: 최적 스레드 수 = CPU 코어 수 * 목표 CPU 사용률 * (1 + 대기시간/처리시간)
  // 큐 크기 = 초당 요청 수 * (평균 처리 시간 + 여유 시간)


  // I/O 집약적 작업용 (API 호출, DB 쿼리 등)
  @Bean
  public ThreadPoolTaskExecutor ioIntensiveExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

    // I/O 대기가 많으므로 스레드 수를 늘림
    executor.setCorePoolSize(20);
    executor.setMaxPoolSize(50);
    executor.setQueueCapacity(1000);
    executor.setKeepAliveSeconds(60); // 사용되지 않은지 60초 후 제거
    executor.setAllowCoreThreadTimeOut(true);  // 유휴 시 코어 스레드도 정리
    executor.setThreadNamePrefix("io-");

    executor.setRejectedExecutionHandler(      // 거부 정책
        new ThreadPoolExecutor.CallerRunsPolicy() // 호출 스레드에서 직접 실행
    );
    executor.setTaskDecorator(new ContextPropagatingTaskDecorator()); // 컨텍스트 전파를 위한 데코레이터 설정
    executor.initialize();

    return executor;
  }

}
