package com.sprint.mission.discodeit.handle;

import com.sprint.mission.discodeit.dto.data.NotificatonDto;
import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AsyncNotificationSender {

  private final NotificationRepository notificationRepository;

  @Async
  @Retryable(
      value = { Exception.class },
      maxAttempts = 3, //세번하고 2초간격이로고
      backoff = @Backoff(delay = 2000)
  )
  public void send(NotificatonDto event) {
    Notification notification = new Notification(
        event.receviedId(),
        event.title(),
        event.type(),
        event.targetId().orElse(null)
    );
    notificationRepository.save(notification);
  }
}