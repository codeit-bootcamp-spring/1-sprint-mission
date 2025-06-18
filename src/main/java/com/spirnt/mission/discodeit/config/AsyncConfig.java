package com.spirnt.mission.discodeit.config;

import com.spirnt.mission.discodeit.async.PropagatingTaskDecorator;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    // Spring이 사용할 기본 Executor 지정
    // @Async 어노테이션에 별도 명시가 없는 경우 이 Executor 가 사용됨
    @Override
    public Executor getAsyncExecutor() {
        return defaultExecutor();  // 기본 실행자 지정
    }

    // 기본 비동기 실행자
    @Bean("defaultExecutor")
    public ThreadPoolTaskExecutor defaultExecutor() {
        // 현재 런타임 중에 사용할 수 있는 코어 수 
        int cores = Runtime.getRuntime().availableProcessors();

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(cores);    // 기본 스레드 수(요청이 없어도 살아있음)
        executor.setMaxPoolSize(cores);    // 최대 스레드 수
        executor.setQueueCapacity(1000);    // 대기열 크기
        executor.setThreadNamePrefix("default-");   // 스레드 이름 접두사
        // 스레드 최대치, 큐 포화 상태일 때 정책
        executor.setRejectedExecutionHandler(
            // 이 작업을 요청한 스레드가 실행해라
            new CallerRunsPolicy()
        );
        executor.initialize();
        return executor;
    }

    @Bean("propagatingExecutor")
    public ThreadPoolTaskExecutor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(8);
        executor.setThreadNamePrefix("async-");
        executor.setTaskDecorator(new PropagatingTaskDecorator());
        executor.initialize();
        return executor;
    }

}
