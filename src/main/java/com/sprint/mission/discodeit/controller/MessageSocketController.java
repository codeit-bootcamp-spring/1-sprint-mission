package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.message.MessageRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MessageSocketController {

  private final MessageService messageService;
  private final SimpMessagingTemplate messagingTemplate;

  @MessageMapping("/messages")
  public void sendMessage(
      @Payload MessageRequestDto chatMessage) {
    MessageResponseDto response = messageService.create(chatMessage);

    String destination = "/sub/channels." + chatMessage.channelId() + ".messages";
    log.info("🐰 broadcast destination: {} / response: {}", destination, response);
    messagingTemplate.convertAndSend(destination, response);
  }

}
