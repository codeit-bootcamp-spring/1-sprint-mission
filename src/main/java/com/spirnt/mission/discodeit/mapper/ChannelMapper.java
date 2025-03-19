package com.spirnt.mission.discodeit.mapper;

import com.spirnt.mission.discodeit.dto.channel.ChannelDto;
import com.spirnt.mission.discodeit.dto.user.UserDto;
import com.spirnt.mission.discodeit.enity.Channel;
import java.time.Instant;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface ChannelMapper {

  // 이 둘은 채널 서비스에서 전달
  @Mapping(target = "participants", source = "participants")
  @Mapping(target = "lastMessageAt", source = "lastMessageAt")
  ChannelDto toDto(Channel channel, List<UserDto> participants, Instant lastMessageAt);
}