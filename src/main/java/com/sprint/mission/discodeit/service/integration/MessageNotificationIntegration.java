package com.sprint.mission.discodeit.service.integration;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.event.NewMessageEvent;
import com.sprint.mission.discodeit.event.notification.NotificationEvent;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageNotificationIntegration {

  private final ChannelRepository channelRepository;
  private final ApplicationEventPublisher eventPublisher;

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handleNewMessageEvent(NewMessageEvent event) {
    MessageDto message = event.messageDto();
    UUID channelId = message.channelId();
    UUID authorId = message.author().id();

    Set<UUID> channelMemberIds = channelRepository.findMemberIdsByChannelId(channelId);

    channelMemberIds.stream()
        .filter(memberId -> !memberId.equals(authorId))
        .forEach(memberId -> {
          NotificationEvent notificationEvent = NotificationEvent.newMessage(
              memberId,
              channelId,
              String.format("새 메시지: %s", message.author().username()),
              message.content()
          );

          eventPublisher.publishEvent(notificationEvent);

          log.debug("메시지 알림 이벤트 발행: receiverId={}, channelId={}, messageId={}",
              memberId, channelId, message.id());
        });
  }
}