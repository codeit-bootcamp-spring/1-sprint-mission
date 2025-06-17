package com.sprint.mission.discodeit.event.notification;

import com.sprint.mission.discodeit.entity.AsyncTaskFailure;
import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.entity.NotificationType;
import com.sprint.mission.discodeit.entity.UploadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.time.Instant;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationEventListener {

  private final NotificationRepository notificationRepository;
  private final UserRepository userRepository;

  @Async
  @Retryable(
      value = { Exception.class },
      maxAttempts = 3,
      backoff = @Backoff(delay = 1000)
  )
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handleNotification(NotificationEvent event) {
    log.info("알림 이벤트 처리 중: {}", event);

    User user = userRepository.findById(event.userId())
        .orElseThrow(() -> new RuntimeException("사용자 없음: " + event.userId()));

    Notification notification = new Notification(
        user,
        event.title(),
        event.content(),
        event.type(),
        event.targetId(),
        Instant.now()
    );
    notificationRepository.save(notification);

    log.info("알림 저장 완료: {}", notification.getId());
  }

  @Recover
  public void recover(Exception e, NotificationEvent event) {
    log.error("알림 생성 실패 (최종 복구): {}", event, e);

    Notification notification = new Notification(
        userRepository.findById(event.userId()).orElseThrow(UserNotFoundException::new),
        event.title(),
        event.content(),
        event.type(),
        event.targetId(),
        Instant.now()
    );
    notificationRepository.save(notification);
  }
}

