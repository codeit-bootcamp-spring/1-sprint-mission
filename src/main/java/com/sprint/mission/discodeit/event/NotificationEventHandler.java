package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class NotificationEventHandler {

  private final NotificationRepository notificationRepository;
  private final UserRepository userRepository;

  @Async
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  @Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
  public void handleNotificationEvent(NotificationEvent event) {
    var receiver = userRepository.findById(event.getReceiverId())
        .orElseThrow(UserNotFoundException::new);

    var notification = new Notification(
        receiver,
        event.getTitle(),
        event.getContent(),
        event.getType(),
        event.getTargetId());

    notificationRepository.save(notification);
  }
}