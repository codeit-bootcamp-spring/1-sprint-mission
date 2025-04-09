package com.sprint.mission.discodeit.mapper;


import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.CreateChannelDto;
import com.sprint.mission.discodeit.dto.channel.CreatePrivateChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = UserMapper.class, imports = {Objects.class, ArrayList.class})

public interface ChannelMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "name", source = "name")
  @Mapping(target = "type", constant = "PUBLIC")
  Channel toEntity(CreateChannelDto dto);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "type", constant = "PRIVATE")
  @Mapping(target = "statuses", expression = "java(new ArrayList<>())")
  Channel toEntity(CreatePrivateChannelDto dto);

  @Mapping(source = "channel.id", target = "id")
  @Mapping(source = "channel.type", target = "type")
  @Mapping(target = "name", source = "channel.name", defaultExpression = "java(\"\")")
  @Mapping(target = "description", source = "channel.description", defaultExpression = "java(\"\")")
  @Mapping(target = "lastMessageAt", source = "lastMessageAt", defaultExpression = "java(Instant.EPOCH)")
  @Mapping(target = "participants", source = "participants", defaultExpression = "java(new ArrayList<>())")
  ChannelResponseDto toDto(Channel channel, Instant lastMessageAt, List<User> participants);

  default List<ChannelResponseDto> toListResponse(
      List<Channel> channels,
      Map<UUID, Instant> latestMessageByChannel,
      Map<UUID, List<User>> channelParticipants
  ) {

    return channels.stream()
        .map(channel -> toDto(
            channel,
            latestMessageByChannel.getOrDefault(channel.getId(), Instant.EPOCH),
            channelParticipants.getOrDefault(channel.getId(), Collections.emptyList())
        )).collect(Collectors.toList());
  }

  default Map<UUID, List<User>> channelToParticipants(List<Channel> channels) {
    Map<UUID, List<User>> channelParticipants = new HashMap<>();

    for (Channel channel : channels) {
      channelParticipants.put(channel.getId(), new ArrayList<>());
      for (ReadStatus status : channel.getStatuses()) {
        channelParticipants.get(channel.getId()).add(status.getUser());
      }
    }

    return channelParticipants;
  }
}


