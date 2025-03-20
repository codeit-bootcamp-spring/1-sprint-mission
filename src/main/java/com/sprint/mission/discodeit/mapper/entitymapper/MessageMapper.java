package com.sprint.mission.discodeit.mapper.entitymapper;

import com.sprint.mission.discodeit.dto.response.BinaryContentDto;
import com.sprint.mission.discodeit.dto.response.MessageDto;
import com.sprint.mission.discodeit.dto.response.UserDto;
import com.sprint.mission.discodeit.entity.message.Message;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import java.util.List;

public class MessageMapper {

  public static MessageDto toDto(Message message) {
    if (message == null) {
      throw new MessageNotFoundException();
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
