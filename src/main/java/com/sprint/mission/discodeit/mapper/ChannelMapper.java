package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import java.time.Instant;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChannelMapper {

  private final UserMapper userMapper;

  public ChannelDto toDto(Channel entity) {
    if (entity == null) {
      return null;
    }
    ChannelDto dto = new ChannelDto();
    dto.setId(entity.getId());
    dto.setType(entity.getType());
    dto.setName(entity.getName());
    dto.setDescription(entity.getDescription());
    dto.setParticipants(entity.getReadStatuses().stream()
        .map(readStatus -> userMapper.toDto(readStatus.getUser()))
        .collect(Collectors.toList()));
    dto.setLastMessageAt(entity.getMessages().stream()
        .map(message -> message.getCreatedAt())
        .max(Instant::compareTo)
        .orElse(null));
    return dto;
  }
}
