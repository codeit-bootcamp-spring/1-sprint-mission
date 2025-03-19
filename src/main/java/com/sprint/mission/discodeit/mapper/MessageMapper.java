package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageMapper {

  private final BinaryContentMapper binaryContentMapper;
  private final UserMapper userMapper;

  public MessageDto toDto(Message message) {
    return new MessageDto(
        message.getId(),
        message.getCreatedAt(),
        message.getUpdatedAt(),
        userMapper.toDto(message.getAuthor()),
        message.getContent(),
        message.getChannel().getId(),
        message.getAttachments().stream()
            .map(ma -> binaryContentMapper.toDto(ma.getContent()))
            .toList());
  }
}
