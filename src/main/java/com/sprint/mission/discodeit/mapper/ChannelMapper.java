package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ChannelMapper {

  private final UserMapper userMapper;

  public ChannelMapper(UserMapper userMapper) {
    this.userMapper = userMapper;
  }

  public ChannelDto toDto(Channel channel) {
    if (channel == null) {
      return null;
    }

    List<UserDto> participants = channel.getReadStatuses().stream()
        .map(ReadStatus::getUser)
        .map(userMapper::toDto)
        .toList();

    Instant lastMessageAt = getLastMessageTime(channel);

    return new ChannelDto(
        channel.getId(),
        channel.getType(),
        channel.getName(),
        channel.getDescription(),
        participants,
        lastMessageAt
    );
  }

  public List<ChannelDto> toDtoList(List<Channel> channels) {
    if (channels == null) {
      return Collections.emptyList();
    }

    return channels.stream()
        .map(this::toDto)
        .toList();
  }

  private Instant getLastMessageTime(Channel channel) {
    return channel.getMessages().stream()
        .map(Message::getCreatedAt)
        .max(Instant::compareTo)
        .orElse(null);
  }
}
