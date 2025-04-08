package com.sprint.mission.discodeit.mapper.entitymapper;

import com.sprint.mission.discodeit.dto.response.BinaryContentDto;
import com.sprint.mission.discodeit.dto.response.MessageDto;
import com.sprint.mission.discodeit.dto.response.UserDto;
import com.sprint.mission.discodeit.entity.message.Message;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import java.time.Instant;
import java.util.List;
import java.util.Map;

public class MessageMapper {

  public static MessageDto toDto(Message message) {
    if (message == null) {
      throw new MessageNotFoundException(Instant.now(), ErrorCode.MESSAGE_NOT_FOUND,
          Map.of(ErrorCode.MESSAGE_NOT_FOUND.getCode(), ErrorCode.MESSAGE_NOT_FOUND.getMessage())
      );
    }

    List<BinaryContentDto> attachments = message.getAttachments().stream()
        .map(BinaryContentMapper::toDto)
        .toList();

    UserDto author = UserMapper.toDto(message.getAuthor());

    return new MessageDto(
        message.getId(),
        message.getCreatedAt(),
        message.getUpdatedAt(),
        message.getContent(),
        message.getChannel().getId(),
        author,
        attachments
    );
  }
}
