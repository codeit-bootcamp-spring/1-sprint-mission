package com.sprint.mission.discodeit.event.listener;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.event.NewMessageEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

  private final SimpMessagingTemplate messagingTemplate;

  @EventListener
  public void handleNewMessageEvent(NewMessageEvent event) {
    MessageDto message = event.messageDto();

    String destination = String.format("/sub/channels.%s.messages",
        message.channelId());
    messagingTemplate.convertAndSend(destination, message);

    log.debug("NewMessageEvent를 웹소켓으로 전송: messageId={}, channelId={}",
        message.id(), message.channelId());
  }
}