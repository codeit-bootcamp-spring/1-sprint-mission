package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.channel.*;
import com.sprint.mission.discodeit.entity.Channel;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class ChannelMapper {
  public Channel toEntity(CreateChannelDto channelDto){
    return new Channel.ChannelBuilder(channelDto.channelName(), channelDto.channelType())
        .maxNumberOfPeople(channelDto.maxNumberOfPeople())
        .isPrivate(false)
        .serverUUID(channelDto.serverId())
        .build();
  }

  public Channel toEntity(CreatePrivateChannelDto channelDto){
    return new Channel.ChannelBuilder(null, channelDto.channelType())
        .maxNumberOfPeople(channelDto.maxNumberOfPeople())
        .isPrivate(true)
        .serverUUID(channelDto.serverId())
        .participatingUsers(channelDto.userIds())
        .build();
  }

  public PublicChannelResponseDto toPublicDto(Channel channel){
    return new PublicChannelResponseDto(
        channel.getUUID(),
        channel.getServerUUID(),
        channel.getChannelType(),
        channel.getChannelName(),
        channel.getCreatedAt(),
        false
    );
  }

  public PrivateChannelResponseDto toPrivateDto(Channel channel){
    return new PrivateChannelResponseDto(
        channel.getUUID(),
        channel.getServerUUID(),
        channel.getChannelType(),
        channel.getCreatedAt(),
        channel.getParticipatingUsers()
    );
  }

  public FindChannelResponseDto toFindChannelDto(Channel channel, Instant lastMessagedAt, List<String> userIds){
    boolean isPrivate = channel.getIsPrivate();
    return new FindChannelResponseDto(
        channel.getUUID(),
        isPrivate ? null : channel.getChannelName(),
        channel.getServerUUID(),
        channel.getChannelType(),
        channel.getCreatedAt(),
        lastMessagedAt,
        userIds,
        channel.getMaxNumberOfPeople()
    );
  }
}
