package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.response.BinaryContentResponse;
import com.sprint.mission.discodeit.dto.response.MessageResponse;
import com.sprint.mission.discodeit.entity.Message;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageMapper {

  private final UserMapper userMapper;
  private final BinaryContentMapper binaryContentMapper;

  public MessageResponse entityToDto(Message message) {
    return MessageResponse.builder()
        .id(message.getId())
        .author(userMapper.entityToDto(message.getAuthor()))
        .channelId(message.getChannel().getId())
        .content(message.getContent())
        .createdAt(message.getCreatedAt())
        .updatedAt(message.getUpdatedAt())
        .attachments(getAttachments(message))
        .build();
  }

  private List<BinaryContentResponse> getAttachments(Message message) {
    return message.getAttachments().stream()
        .map(binaryContentMapper::entityToDto)
        .collect(Collectors.toList());
  }
}
