package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import org.mapstruct.*;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface ChannelMapper {

  @Mapping(target = "participants", expression = "java(getParticipants(channel, userMapper))")
  @Mapping(target = "lastMessageAt", expression = "java(getLastMessageTime(channel))")
  ChannelDto toDto(Channel channel, @Context UserMapper userMapper);

  default List<ChannelDto> toDtoList(List<Channel> channels, @Context UserMapper userMapper) {
    if (channels == null) {
      return null;
    }

    return channels.stream()
        .map(channel -> toDto(channel, userMapper))
        .collect(Collectors.toList());
  }

  default List<UserDto> getParticipants(Channel channel, UserMapper userMapper) {
    if (channel == null) {
      return null;
    }

    List<User> users;
    if (ChannelType.PRIVATE.equals(channel.getType())) {
      users = channel.getReadStatuses().stream()
          .map(ReadStatus::getUser)
          .distinct()
          .collect(Collectors.toList());
    } else {
      users = channel.getMessages().stream()
          .map(Message::getAuthor)
          .distinct()
          .collect(Collectors.toList());
    }

    return users.stream()
        .map(userMapper::toDto)
        .collect(Collectors.toList());
  }

  default Instant getLastMessageTime(Channel channel) {
    if (channel == null || channel.getMessages() == null || channel.getMessages().isEmpty()) {
      return null;
    }

    return channel.getMessages().stream()
        .map(Message::getCreatedAt)
        .max(Instant::compareTo)
        .orElse(null);
  }
}