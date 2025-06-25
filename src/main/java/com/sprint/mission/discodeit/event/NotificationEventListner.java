package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.dto.NotificationDto;
import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.service.basic.NotificationService;
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
public class NotificationEventListner {

  private final NotificationService notificationService;

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  @Async
  @Retryable(
      maxAttempts = 3,
      backoff = @Backoff(delay = 2000)
  )
  public void handleNotificationCreateEvent(NotificationDto notificationDto) {
    log.info("비동기 알림 생성 시작 : type={}", notificationDto.getType());

    try {
      notificationService.create(notificationDto);

      log.info("알림 생성 및 저장 성공");
    } catch (Exception e) {
      log.error("알림 생성 중 최대 재시도 횟수 초과 or 오류 발생 : type={}", notificationDto.getType(), e);
      throw e;
    }

  }

}
