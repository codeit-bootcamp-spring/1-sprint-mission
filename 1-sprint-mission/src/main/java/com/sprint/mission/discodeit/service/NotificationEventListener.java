package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.configure.NewMessageEvent;
import com.sprint.mission.discodeit.configure.NotificationEvent;
import com.sprint.mission.discodeit.service.basic.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
public class NotificationEventListener {

  private final NotificationService notificationService;

  @Async
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  @Retryable(
      value = Exception.class,
      maxAttempts = 3,
      backoff = @Backoff(delay = 1000)
  )
  public void handleNotificationEvent(NotificationEvent event) {
    boolean enabled = true;
    if (event instanceof NewMessageEvent) {
      enabled = notificationService.isNotificationEnabled(event.getUserId(), event.getTargetId());
    }
    if (!enabled) {
      return;
    }

    notificationService.createNotification(event.getUserId(), event.getType(), event.getTargetId());
  }

  @Recover
  public void recover(Exception e, NotificationEvent event) {
    notificationService.recordFailure(event, e);
  }
}
