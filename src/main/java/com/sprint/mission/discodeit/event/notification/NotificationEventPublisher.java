package com.sprint.mission.discodeit.event.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationEventPublisher {

  private final ApplicationEventPublisher publisher;

  public void publish(NotificationEvent event) {
    publisher.publishEvent(event);
  }
}
