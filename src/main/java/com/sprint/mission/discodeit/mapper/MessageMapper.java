package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.MessageAttachment;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class MessageMapper {

  private final UserMapper userMapper;
  private final BinaryContentMapper binaryContentMapper;

  public MessageMapper(UserMapper userMapper, BinaryContentMapper binaryContentMapper) {
    this.userMapper = userMapper;
    this.binaryContentMapper = binaryContentMapper;
  }

  public MessageDto toDto(Message message) {
    if (message == null) {
      return null;
    }

    List<BinaryContentDto> attachmentDtos = message.getAttachments() != null ?
        message.getAttachments().stream()
            .filter(att -> att != null && att.getAttachment() != null)
            .map(MessageAttachment::getAttachment)
            .map(binaryContentMapper::toDto)
            .collect(Collectors.toList()) :
        Collections.emptyList();

    return new MessageDto(
        message.getId(),
        message.getCreatedAt(),
        message.getUpdatedAt(),
        message.getContent(),
        message.getChannel() != null ? message.getChannel().getId() : null,
        userMapper.toDto(message.getAuthor()),
        attachmentDtos
    );
  }
}