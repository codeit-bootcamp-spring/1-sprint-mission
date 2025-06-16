package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.response.BinaryContentDto;
import com.sprint.mission.discodeit.dto.response.MessageDto;
import com.sprint.mission.discodeit.dto.response.UserDto;
import com.sprint.mission.discodeit.entity.message.MessageCreateRequest;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.repository.UserRepository;
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

  @MessageMapping("/messages")
  public void send(MessageCreateRequest request) {
    
    User user = userRepository.findById(request.authorId())
        .orElse(null);

    BinaryContentDto binaryContentDto;
    if (user.getProfile() == null) {
      binaryContentDto = null;
    } else {
      binaryContentDto = new BinaryContentDto(user.getProfile().getId(),
          user.getProfile().getFileName(),
          user.getProfile()
              .getSize(), user.getProfile().getContentType());
    }

    UserDto userDto = UserDto.builder()
        .id(user.getId())
        .username(user.getUsername())
        .email(user.getEmail())
        .profile(binaryContentDto)
        .online(userSessionService.isOnline(user.getUsername()))
        .Role(user.getRole())
        .build();

    MessageDto message = MessageDto.builder()
        .content(request.content())
        .channelId(request.channelId())
        .author(userDto)
        .build();

    messagingTemplate.convertAndSend("/sub/channels." + request.channelId() + ".messages", message);
  }
}
