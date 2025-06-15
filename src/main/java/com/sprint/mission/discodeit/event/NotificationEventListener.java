package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.dto.data.NotificationEvent;
import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationEventListener {

  private final NotificationRepository notificationRepository;

  @Async
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handle(NotificationEvent event) {
    Notification notification = new Notification(event.receiverId(), event.type(), event.targetId(), event.message());

    notificationRepository.save(notification);
  }
}

