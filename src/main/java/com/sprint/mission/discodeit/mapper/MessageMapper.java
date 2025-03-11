package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageMapper {

  private final BinaryContentMapper binaryContentMapper;
  private final BinaryContentRepository binaryContentRepository;
  private final UserMapper userMapper;

  public MessageDto toDto(Message entity) {
    if (entity == null) {
      return null;
    }
    MessageDto dto = new MessageDto();
    dto.setId(entity.getId());
    dto.setCreatedAt(entity.getCreatedAt());
    dto.setUpdatedAt(entity.getUpdatedAt());
    dto.setContent(entity.getContent());
    dto.setChannelId(entity.getChannel().getId());
    dto.setAuthor(userMapper.toDto(entity.getAuthor()));
    dto.setAttachments(entity.getAttachmentIds().stream()
        .map(binaryContentRepository::findById)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .map(binaryContentMapper::toDto)
        .collect(Collectors.toList()));
    return dto;
  }
}
