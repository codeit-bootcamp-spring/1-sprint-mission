package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class WebSocketMessageController {

  private final MessageService messageService;
  private final SimpMessagingTemplate messagingTemplate;

  @MessageMapping("/messages")
  public void handleMessage(MessageCreateRequest request) {
    log.debug("웹소켓 메시지 수신: channelId={}, authorId={}",
        request.channelId(), request.authorId());

    try {
      MessageDto createdMessage = messageService.create(
          request,
          Collections.emptyList()
      );

      String destination = String.format("/sub/channels.%s.messages",
          request.channelId());
      messagingTemplate.convertAndSend(destination, createdMessage);

      log.info("웹소켓 메시지 전송 완료: messageId={}, channelId={}",
          createdMessage.id(), request.channelId());
    } catch (Exception e) {
      log.error("웹소켓 메시지 처리 중 오류 발생", e);
    }
  }
}