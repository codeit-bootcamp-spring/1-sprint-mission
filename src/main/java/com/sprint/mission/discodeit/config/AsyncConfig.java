package com.sprint.mission.discodeit.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
public class AsyncConfig {

  // 기본적으로 ThreadPoolTaskExecutor 사용한다.(추가 설정 없어도 됨 - 단, 최적화는 안된 상태!)

  //ThreadPoolTaskExecutor: 스레드 풀 관리 클래스
  // - 여러 개의 스레드를 미리 만들어 놓고 작업을 배분하는 역할을 한다.
  // - 기본 설정:
  //    corePoolSize(항상 유지되는 스레드 수)  / maxPoolSize(최대 스레드 수) / queueCapacity(대기열 크기)


}
