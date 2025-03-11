package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.channel.ChannelResponse;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {UserMapper.class})
public interface ChannelMapper {

  ChannelMapper INSTANCE = Mappers.getMapper(ChannelMapper.class);

  default List<UserResponse> map(List<User> users) {
    return users.stream().map(UserMapper.INSTANCE::userToUserResponse).toList();
  }

  default UUID map(Channel channel) {
    return channel.getId();
  }

  @Mapping(source = "id", target = "channelId")
  @Mapping(source = "private", target = "isPrivate")
  @Mapping(source = "users", target = "userList")
  ChannelResponse toChannelResponse(Channel channel);
}
