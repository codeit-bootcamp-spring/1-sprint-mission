package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.common.async.ContextPropagatingTaskDecorator;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    // 기본 비동기 실행자
    @Bean("defaultExecutor")
    public ThreadPoolTaskExecutor defaultExecutor() {
        int cores = Runtime.getRuntime().availableProcessors();

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(cores);           // 기본 스레드 수
        executor.setMaxPoolSize(cores * 2);        // 최대 스레드 수
        executor.setQueueCapacity(1000);           // 대기열 크기
        executor.setThreadNamePrefix("default-");  // 스레드 이름 접두사
        executor.setRejectedExecutionHandler(      // 거부 정책
            new ThreadPoolExecutor.CallerRunsPolicy()
        );
        executor.setTaskDecorator(new ContextPropagatingTaskDecorator()); // 컨텍스트 전파
        executor.initialize();
        return executor;
    }

    @Override
    public Executor getAsyncExecutor() {
        return defaultExecutor();
    }
}
