package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventListener {

  private final NotificationService notificationService;

  @Async("eventTaskExecutor")
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  @Retryable(
      retryFor = {Exception.class},
      noRetryFor = {IllegalArgumentException.class},
      maxAttempts = 3,
      backoff = @Backoff(delay = 1000, multiplier = 2)
  )
  public void handle(NotificationEvent event) {
    try {
      log.info("알림 생성 이벤트 발생: {}", event);
      notificationService.create(event);
      log.info("알림 생성 완료: userId = {}", event.getReceiverId());
    } catch (Exception e) {
      log.error("알림 생성 이벤트 처리 실패: {} (재시도 시작 됨)", event, e);
      throw e; // 재시도를 위해 예외를 다시 던진다
    }
  }
}
