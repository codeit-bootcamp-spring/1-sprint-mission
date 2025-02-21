package com.sprint.mission.discodeit.mapper;


import com.sprint.mission.discodeit.dto.channel.*;
import com.sprint.mission.discodeit.entity.Channel;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = false), imports = Objects.class)
public interface ChannelMapper {
  @Mapping(target = "UUID", ignore = true)
  @Mapping(target = "channelName", source = "name")
  @Mapping(target = "channelType", constant = "PUBLIC")
  @Mapping(target = "participatingUsers", expression = "java(new java.util.ArrayList<>())")
  @Mapping(target = "createdAt", expression = "java(Instant.now())")
  @Mapping(target = "updatedAt", expression = "java(Instant.now())")
  Channel toEntity(CreateChannelDto dto);

  @Mapping(target = "UUID", ignore = true)
  @Mapping(target = "channelType", constant = "PRIVATE")
  @Mapping(source = "participantIds", target = "participatingUsers")
  @Mapping(target = "createdAt", expression = "java(Instant.now())")
  @Mapping(target = "updatedAt", expression = "java(Instant.now())")
  Channel toEntity(CreatePrivateChannelDto dto);

  @Mapping(source = "UUID", target = "id")
  @Mapping(source = "channelType", target = "type")
  @Mapping(source = "channelName", target = "name")
  @Mapping(source = "createdAt", target = "createdAt")
  @Mapping(source = "updatedAt", target = "updatedAt")
  @Mapping(source = "description", target = "description")
  PublicChannelResponseDto toPublicDto(Channel channel);

  @Mapping(source = "UUID", target = "id")
  @Mapping(source = "channelType", target = "type")
  @Mapping(source = "participatingUsers", target = "participantIds")
  PrivateChannelResponseDto toPrivateDto(Channel channel);

  @Mapping(source = "UUID", target = "channelId")
  @Mapping(source = "channelType", target = "channelType")
  @Mapping(source = "createdAt", target = "createdAt")
  @Mapping(target = "channelName", expression = "java(Objects.equals(channel.getChannelType(), Channel.ChannelType.PRIVATE) ? null : channel.getChannelName())")
  FindChannelResponseDto toFindChannelDto(Channel channel);

  default FindChannelResponseDto toFindChannelDto(Channel channel,  Instant lastMessagedAt, List<String> userIds) {
    FindChannelResponseDto dto = toFindChannelDto(channel);
    return new FindChannelResponseDto(
        dto.channelId(),
        dto.channelName(),
        dto.serverId(),
        dto.channelType(),
        dto.isPrivate(),
        dto.createdAt(),
        lastMessagedAt,
        userIds,
        dto.maxNumberOfPeople()
    );
  }

}
