package com.sprint.mission.discodeit.event;


import com.sprint.mission.discodeit.event.event_entity.NotificationEvent;
import com.sprint.mission.discodeit.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventListener {

  private final NotificationService notificationService;

  @Async("defaultExecutor")
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handleNotificationEvent(NotificationEvent event) {
    log.info("[NOTIFICATION EVENT] Handling : {}", event.title());

    notificationService.createNotificationsFromEvent(event);
  }
}
