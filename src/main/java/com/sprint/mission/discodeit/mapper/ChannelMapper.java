package com.sprint.mission.discodeit.mapper;


import com.sprint.mission.discodeit.dto.channel.*;
import com.sprint.mission.discodeit.entity.Channel;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ChannelMapper {
  @Mapping(target = "UUID", ignore = true)
  @Mapping(target = "isPrivate", constant = "false")
  @Mapping(source = "serverId", target = "serverUUID")
  @Mapping(target = "participatingUsers", expression = "java(new java.util.ArrayList<>())") // 빈 리스트 초기화
  @Mapping(target = "createdAt", expression = "java(Instant.now())")
  @Mapping(target = "updatedAt", expression = "java(Instant.now())")
  Channel toEntity(CreateChannelDto dto);

  @Mapping(target = "UUID", ignore = true)
  @Mapping(source = "channelType", target = "channelType")
  @Mapping(source = "maxNumberOfPeople", target = "maxNumberOfPeople")
  @Mapping(target = "isPrivate", constant = "true")
  @Mapping(source = "serverId", target = "serverUUID")
  @Mapping(source = "userIds", target = "participatingUsers")
  @Mapping(target = "createdAt", expression = "java(Instant.now())")
  @Mapping(target = "updatedAt", expression = "java(Instant.now())")
  Channel toEntity(CreatePrivateChannelDto dto);

  @Mapping(source = "UUID", target = "channelId")
  @Mapping(source = "serverUUID", target = "serverId")
  @Mapping(source = "channelType", target = "channelType")
  @Mapping(source = "channelName", target = "channelName")
  @Mapping(source = "createdAt", target = "createdAt")
  @Mapping(target = "isPrivate", constant = "false") // 공개 채널이므로 false
  PublicChannelResponseDto toPublicDto(Channel channel);

  @Mapping(source = "UUID", target = "channelId")
  @Mapping(source = "serverUUID", target = "serverId")
  @Mapping(source = "channelType", target = "channelType")
  @Mapping(source = "createdAt", target = "createdAt")
  @Mapping(source = "participatingUsers", target = "participants")
  PrivateChannelResponseDto toPrivateDto(Channel channel);

  @Mapping(source = "UUID", target = "channelId")
  @Mapping(source = "serverUUID", target = "serverId")
  @Mapping(source = "channelType", target = "channelType")
  @Mapping(source = "createdAt", target = "createdAt")
  @Mapping(source = "maxNumberOfPeople", target = "maxNumberOfPeople")
  @Mapping(target = "channelName", expression = "java(channel.getIsPrivate() ? null : channel.getChannelName())")
  FindChannelResponseDto toFindChannelDto(Channel channel, @Context Instant lastMessagedAt, @Context List<String> userIds);


}
