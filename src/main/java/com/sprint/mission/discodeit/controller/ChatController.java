package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.message.MessageCreateRequest;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.chat.ChatService;
import com.sprint.mission.discodeit.service.status.UserSessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {

  private final SimpMessagingTemplate messagingTemplate;
  private final UserRepository userRepository;
  private final UserSessionService userSessionService;
  private final ChatService chatService;

  @MessageMapping("/messages")
  public void send(MessageCreateRequest request) {
    chatService.send(request);
  }
}
