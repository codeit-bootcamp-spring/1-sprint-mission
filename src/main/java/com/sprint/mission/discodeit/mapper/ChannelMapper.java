package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.base.BaseEntity;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import java.time.Instant;
import java.util.Comparator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChannelMapper {
  private final MessageRepository messageRepository;
  private final ReadStatusRepository readStatusRepository;

  private final UserMapper userMapper;

  public ChannelDto toDto(Channel channel) {
    return new ChannelDto(
        channel.getId(),
        channel.getType(),
        channel.getName(),
        channel.getDescription(),
        readStatusRepository.findByChannelId(channel.getId())
            .stream()
            .map(ReadStatus::getUser)
            .map(userMapper::toDto)
            .toList(),
        messageRepository.findByChannelId(channel.getId())
            .stream()
            .max(Comparator.comparing(BaseEntity::getCreatedAt))
            .map(BaseEntity::getCreatedAt)
            .orElse(Instant.MIN)
    );
  }
}
