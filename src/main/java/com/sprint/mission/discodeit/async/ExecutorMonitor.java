package com.sprint.mission.discodeit.async;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ExecutorMonitor {

  private final ThreadPoolTaskExecutor executor;

  public ExecutorMonitor(@Qualifier("defaultExecutor") ThreadPoolTaskExecutor executor) {
    this.executor = executor;
  }

  @EventListener(ApplicationReadyEvent.class)
  public void monitor() {
    ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    scheduler.scheduleAtFixedRate(() -> {
          try {
            ThreadPoolExecutor poolExecutor = executor.getThreadPoolExecutor();

            log.info("스레드 Pool 상태: - Active:{}/{}, Queue:{}/{}, Completed:{}",
                poolExecutor.getActiveCount(), executor.getPoolSize(),
                poolExecutor.getQueue().size(), executor.getQueueCapacity(),
                poolExecutor.getCompletedTaskCount());
          } catch (Exception e) {
            log.error("ExecutorMonitor.monitor() <UNK>", e);
          }
        },
        0, 3, TimeUnit.SECONDS);
  }

}
