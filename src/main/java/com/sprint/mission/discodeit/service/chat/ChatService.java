package com.sprint.mission.discodeit.service.chat;

import com.sprint.mission.discodeit.dto.response.BinaryContentDto;
import com.sprint.mission.discodeit.dto.response.MessageDto;
import com.sprint.mission.discodeit.dto.response.UserDto;
import com.sprint.mission.discodeit.entity.message.MessageCreateRequest;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.status.UserSessionService;
import java.time.Instant;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {

  private final MessageService messageService;
  private final UserRepository userRepository;
  private final UserSessionService userSessionService;
  private final SimpMessagingTemplate messagingTemplate;

  public void send(MessageCreateRequest request) {
    User user = userRepository.findById(request.authorId())
        .orElseThrow(() -> new UserNotFoundException(Instant.now(), ErrorCode.USER_NOT_FOUND,
            Map.of(
                ErrorCode.USER_NOT_FOUND.getCode(),
                ErrorCode.USER_NOT_FOUND.getMessage()
            )));

    BinaryContentDto binaryContentDto = toDto(user);

    UserDto userDto = toDto(user, binaryContentDto);
    MessageDto message = toDto(request, userDto);
    messageService.create(request, null);

    messagingTemplate.convertAndSend("/sub/channels." + request.channelId() + ".messages", message);
  }

  private BinaryContentDto toDto(User user) {
    BinaryContentDto binaryContentDto;
    if (user.getProfile() == null) {
      binaryContentDto = null;
    } else {
      binaryContentDto = new BinaryContentDto(user.getProfile().getId(),
          user.getProfile().getFileName(),
          user.getProfile()
              .getSize(), user.getProfile().getContentType());
    }
    return binaryContentDto;
  }

  private MessageDto toDto(MessageCreateRequest request, UserDto userDto) {
    return MessageDto.builder()
        .content(request.content())
        .channelId(request.channelId())
        .author(userDto)
        .build();
  }

  private UserDto toDto(User user, BinaryContentDto binaryContentDto) {
    return UserDto.builder()
        .id(user.getId())
        .username(user.getUsername())
        .email(user.getEmail())
        .profile(binaryContentDto)
        .online(userSessionService.isOnline(user.getUsername()))
        .Role(user.getRole())
        .build();
  }
}
