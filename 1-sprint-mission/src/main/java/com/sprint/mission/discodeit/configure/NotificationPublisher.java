package com.sprint.mission.discodeit.configure;

import java.util.UUID;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class NotificationPublisher {

  private final ApplicationEventPublisher publisher;

  public NotificationPublisher(ApplicationEventPublisher publisher) {
    this.publisher = publisher;
  }

  public void publishNewMessage(UUID userId, UUID channelId) {
    publisher.publishEvent(new NewMessageEvent(userId, channelId));
  }

  public void publishRoleChanged(UUID userId) {
    publisher.publishEvent(new RoleChangedEvent(userId));
  }

  public void publishAsyncFailed(UUID userId) {
    publisher.publishEvent(new AsyncFailedEvent(userId));
  }
}
