package com.sprint.mission.discodeit.config;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    // 기본
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
        executor.initialize();
        return executor;
    }

    @Bean("fileUploadTaskExecutor")
    public ThreadPoolTaskExecutor fileUploadTaskExecutor(
        ContextPropagatingTaskDecorator decorator) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(25);
        executor.setThreadNamePrefix("binary-storage-");
        executor.setTaskDecorator(decorator);   // MDC, SecurityContext 유지하기
        executor.setRejectedExecutionHandler(      // 거부 정책
            new ThreadPoolExecutor.CallerRunsPolicy()
        );
        executor.initialize();
        return executor;
    }

    @Bean(name = "notificationExecutor")
    public Executor notificationExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("notification-");
        executor.setRejectedExecutionHandler(      // 거부 정책
            new ThreadPoolExecutor.CallerRunsPolicy()
        );
        executor.initialize();
        return executor;
    }

    @Bean("eventExecutor")
    public ThreadPoolTaskExecutor eventExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(1000);
        executor.setThreadNamePrefix("sse-event-");
        executor.setRejectedExecutionHandler(      // 거부 정책
            new ThreadPoolExecutor.CallerRunsPolicy()
        );
        executor.initialize();
        return executor;
    }

    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(2); // 동시에 2개까지 실행
        scheduler.setThreadNamePrefix("scheduler-");
        scheduler.setRemoveOnCancelPolicy(true);
        scheduler.initialize();
        return scheduler;
    }

    // 기본 Executor 지정
    @Override
    public Executor getAsyncExecutor() {
        return defaultExecutor();
    }
}
