package com.sprint.mission.discodeit.exception;

import java.lang.reflect.Method;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

  // 비동기 메서드 예외 처리
  @Override
  public void handleUncaughtException(Throwable ex, Method method, Object... params) {
    log.error("비동기 메서드 예외 발생 - Method: {}, Params: {}", method.getName(), Arrays.toString(params),
        ex);
  }
}
