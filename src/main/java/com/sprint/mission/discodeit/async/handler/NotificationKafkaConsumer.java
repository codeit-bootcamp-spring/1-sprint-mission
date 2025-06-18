package com.sprint.mission.discodeit.async.handler;

import com.sprint.mission.discodeit.async.event.AsyncFailedNotificationEvent;
import com.sprint.mission.discodeit.async.event.NewMessageNotificationEvent;
import com.sprint.mission.discodeit.async.event.RoleChangedNotificationEvent;
import com.sprint.mission.discodeit.common.NotificationType;
import com.sprint.mission.discodeit.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationKafkaConsumer {

  private final NotificationService notificationService;

  @KafkaListener(topics = "discodeit.role-changed-topic", groupId = "notification-group")
  public void handleRoleChangedEvent(RoleChangedNotificationEvent event) {
    log.info("역할 변경 알림 생성: 수신자={}, 대상={}", event.getReceiverId(), event.getUserId());
    notificationService.create(event.getReceiverId(), event.getUserId(),
        NotificationType.ROLE_CHANGED);
  }

  @KafkaListener(topics = "discodeit.new-message-topic", groupId = "notification-group")
  public void handleNewMessageEvent(NewMessageNotificationEvent event) {
    log.info("채팅 메시지 알림 생성: 채널={}, 수신자={}", event.getChannelId(), event.getReceiverId());
    notificationService.create(event.getReceiverId(), event.getChannelId(),
        NotificationType.NEW_MESSAGE);
  }

  @KafkaListener(topics = "discodeit.async-failed-topic", groupId = "notification-group")
  public void handleAsyncFailedEvent(AsyncFailedNotificationEvent event) {
    log.error("비동기 작업 실패 알림: 요청={}, 수신자={}", event.getRequestId(), event.getReceiverId());
    notificationService.create(event.getReceiverId(), event.getRequestId(),
        NotificationType.ASYNC_FAILED);
  }
}
