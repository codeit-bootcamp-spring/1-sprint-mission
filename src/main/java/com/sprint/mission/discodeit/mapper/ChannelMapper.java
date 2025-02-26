package com.sprint.mission.discodeit.mapper;


import com.sprint.mission.discodeit.dto.channel.*;
import com.sprint.mission.discodeit.entity.Channel;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;
import java.util.Objects;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = false), imports = Objects.class)
public interface ChannelMapper {
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "channelName", source = "name")
  @Mapping(target = "channelType", constant = "PUBLIC")
  @Mapping(target = "participatingUsers", expression = "java(new java.util.ArrayList<>())")
  @Mapping(target = "createdAt", expression = "java(Instant.now())")
  @Mapping(target = "updatedAt", expression = "java(Instant.now())")
  Channel toEntity(CreateChannelDto dto);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "channelType", constant = "PRIVATE")
  @Mapping(source = "participantIds", target = "participatingUsers")
  @Mapping(target = "createdAt", expression = "java(Instant.now())")
  @Mapping(target = "updatedAt", expression = "java(Instant.now())")
  Channel toEntity(CreatePrivateChannelDto dto);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "channelType", target = "type")
  @Mapping(source = "channelName", target = "name")
  @Mapping(source = "createdAt", target = "createdAt")
  @Mapping(source = "updatedAt", target = "updatedAt")
  @Mapping(source = "description", target = "description")
  PublicChannelResponseDto toPublicDto(Channel channel);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "channelType", target = "type")
  @Mapping(source = "participatingUsers", target = "participantIds")
  PrivateChannelResponseDto toPrivateDto(Channel channel);

  @Mapping(source = "channel.id", target = "id")
  @Mapping(source = "channel.channelType", target = "type")
  @Mapping(target = "name", expression = "java(Objects.equals(channel.getChannelType(), Channel.ChannelType.PRIVATE) ? null : channel.getChannelName())")
  @Mapping(target = "description", source = "channel.description")
  @Mapping(target = "participantIds", source = "channel.participatingUsers")
  @Mapping(target = "lastMessagedAt", source = "lastMessagedAt")
  FindChannelResponseDto toFindChannelDto(Channel channel, Instant lastMessagedAt);


  @Mapping(target = "id", source = "id")
  @Mapping(target = "type", source = "channelType")
  @Mapping(target = "name", source = "channelName")
  UpdateChannelResponseDto toUpdateResponse(Channel channel);

}


