package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.service.Interface.MessageService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class MessageWebSocketController {

    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/messages")
    public void handleMessage(MessageCreateRequest request) {
        MessageDto saved = messageService.createMessage(request, List.of());

        String destination = "/sub/channels." + saved.getChannelId() + ".messages";
        messagingTemplate.convertAndSend(destination, saved);
    }
}
