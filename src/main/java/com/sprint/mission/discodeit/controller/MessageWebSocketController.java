package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.service.MessageService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class MessageWebSocketController {

  private final MessageService messageService;
  private final SimpMessagingTemplate messaging;
  @MessageMapping("/messages")
  public void send(@Valid MessageCreateRequest req) {

    MessageDto dto = messageService.create(req, List.of());

    String dest = "/sub/channels." + dto.channelId() + ".messages";
    messaging.convertAndSend(dest, dto);
  }
}