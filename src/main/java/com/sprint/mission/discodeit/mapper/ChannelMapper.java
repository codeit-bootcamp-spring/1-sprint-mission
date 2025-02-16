package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.channel.CreatePrivateChannelResponse;
import com.sprint.mission.discodeit.dto.channel.CreatePublicChannelResponse;
import com.sprint.mission.discodeit.dto.channel.FindPrivateChannelResponse;
import com.sprint.mission.discodeit.entity.channel.Channel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.Set;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface ChannelMapper {

    @Mappings({
            @Mapping(target = "channelId", source = "channel.id"),
            @Mapping(target = "type", source = "channel.type"),
            @Mapping(target = "channelMembersIds", source = "membersIds")
    })
    CreatePrivateChannelResponse toPrivateResponse(Channel channel, Set<UUID> membersIds);

    @Mappings({
            @Mapping(target = "channelId", source = "channel.id"),
            @Mapping(target = "type", source = "channel.type"),
            @Mapping(target = "name", source = "channel.name"),
            @Mapping(target = "topic", source = "channel.topic")
    })
    CreatePublicChannelResponse toPublicResponse(Channel channel);

    @Mappings({
            @Mapping(target = "recentMessageTime", source = "channel"),
            @Mapping(target = "channelMembersIds", source = "membersIds")
    })
    FindPrivateChannelResponse toFindPrivateResponse(Channel channel, Set<UUID> membersIds);

}