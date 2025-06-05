package com.sprint.mission.discodeit.config;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import org.slf4j.MDC;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

public class MdcTaskExecutor implements AsyncTaskExecutor {

    private final AsyncTaskExecutor delegate;

    public MdcTaskExecutor(ThreadPoolTaskExecutor delegate) {
        this.delegate = delegate;
    }

    @Override
    public void execute(Runnable task) {
        delegate.execute(warp(task, MDC.getCopyOfContextMap()));
    }

    @Override
    public void execute(Runnable task, long startTimeout) {
        delegate.execute(warp(task, MDC.getCopyOfContextMap()), startTimeout);
    }

    @Override
    public Future<?> submit(Runnable task) {
        return delegate.submit(warp(task, MDC.getCopyOfContextMap()));
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return delegate.submit(warp(task, MDC.getCopyOfContextMap()));
    }

    private <T> Callable warp(Callable<T> task, Map<String, String> context) {
        return () -> {
            Map<String, String> previous = MDC.getCopyOfContextMap();
            MDC.setContextMap(context);
            try {
                return task.call();
            } finally {
                if (previous != null) {
                    MDC.setContextMap(previous);
                } else {
                    MDC.clear();
                }
            }
        };
    }

    private Runnable warp(Runnable task, Map<String, String> context) {
        return () -> {
            Map<String, String> previous = MDC.getCopyOfContextMap();
            MDC.setContextMap(context);
            try {
                task.run();
            } finally {
                if (previous != null) {
                    MDC.setContextMap(previous);
                } else {
                    MDC.clear();
                }
            }
        };
    }
}
