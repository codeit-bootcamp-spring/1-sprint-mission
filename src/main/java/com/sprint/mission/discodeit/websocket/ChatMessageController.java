package com.sprint.mission.discodeit.websocket;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.List;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatMessageController {

  private final SimpMessagingTemplate messagingTemplate;
  private final MessageService messageService;

  @MessageMapping("/channels.{channelId}.messages")
  public void handleMessage(MessageDto message) {
    String destination = "/sub/channels." + message.channelId() + ".messages";
    messagingTemplate.convertAndSend(destination, message);
  }

  @MessageMapping("/messages")
  public void sendMessage(MessageCreateRequest request) {
    MessageDto savedMessage = messageService.create(request, List.of());
    String destination = "/sub/channels." + request.channelId() + ".messages";
    messagingTemplate.convertAndSend(destination, savedMessage);
  }
}
