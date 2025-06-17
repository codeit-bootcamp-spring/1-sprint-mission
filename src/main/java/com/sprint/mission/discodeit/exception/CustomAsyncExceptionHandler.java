package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.event.AsyncTaskFailedEvent;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

//void 반환 메서드의 예외 처리
@Component
@Slf4j
@RequiredArgsConstructor
public class CustomAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

  private final ApplicationEventPublisher eventPublisher;

  @Override
  public void handleUncaughtException(Throwable ex, Method method, Object... params) {
    log.error("비동기 메서드 예외 발생 - Method: {}, Params: {}",
        method.getName(), Arrays.toString(params), ex);

    UUID userId = getCurrentUserId();

    if (userId != null) {
      AsyncTaskFailedEvent event = new AsyncTaskFailedEvent(
          userId,
          ex.getMessage()
      );
      eventPublisher.publishEvent(event);
    }
  }

  private UUID getCurrentUserId() {
    try {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      if (authentication != null && authentication.getPrincipal() instanceof DiscodeitUserDetails) {
        DiscodeitUserDetails userDetails = (DiscodeitUserDetails) authentication.getPrincipal();
        return userDetails.getUserDto().id();
      }
    } catch (Exception e) {
      log.debug("SecurityContext에서 유저 정보 가져오기 실패", e);
    }
    return null;
  }
}
