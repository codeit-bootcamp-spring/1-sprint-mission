package com.sprint.mission.dto;

import com.sprint.mission.dto.response.ChannelDto;
import com.sprint.mission.dto.request.PublicChannelCreateDTO;
import com.sprint.mission.dto.response.UserDto;
import com.sprint.mission.entity.Channel;
import com.sprint.mission.entity.ChannelType;
import com.sprint.mission.entity.User;
import org.mapstruct.*;

import java.time.Instant;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ChannelMapper {

  @Mapping(target = "id", source = "channel.id")
  @Mapping(target = "description", source = "channel.description")
  @Mapping(target = "name", source = "channel.name")
  @Mapping(target = "channelType", source = "channel.channelType")
  @Mapping(target = "participants", source = "participants", qualifiedByName = "UserToUserDto")
  @Mapping(target = "lastMessageAt", source = "lastMessageAt")
  ChannelDto toDto(Channel channel, List<User> participants, Instant lastMessageAt);

  @Named("UserToUserDto")
  @Mapping(target = "online", expression = "java(user.getStatus() != null ? user.getStatus().isOnline() : null)")
  UserDto UserToUserDto(User user);

  ChannelDto toDto(Channel channel);

  Channel toPublicEntity(PublicChannelCreateDTO request, ChannelType channelType);

  Channel toPrivateEntity(ChannelType channelType);
}
