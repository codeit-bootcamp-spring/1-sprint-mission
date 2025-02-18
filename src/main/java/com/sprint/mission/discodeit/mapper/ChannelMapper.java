package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.channel.response.CreateChannelResponse;
import com.sprint.mission.discodeit.dto.channel.response.FindChannelResponse;
import com.sprint.mission.discodeit.dto.channel.response.UpdateChannelResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Component
public class ChannelMapper {
    public Channel toEntity(String name, String description, User channelOwner, ChannelType channelType) {
        return Channel.of(name, description, channelOwner, channelType);
    }

    public CreateChannelResponse toCreateChannelResponse(Channel channel) {
        return new CreateChannelResponse(channel.getId(), channel.getName(), channel.getDescription());
    }

    public FindChannelResponse toFindChannelResponse(Channel channel, Instant lastMessageTime, List<UUID> channelUsersIdList) {
        return new FindChannelResponse(channel.getName(), channel.getDescription(), lastMessageTime,
                channel.getChannelType(), channelUsersIdList);
    }

    public UpdateChannelResponse toUpdateChannelResponse(Channel channel) {
        return new UpdateChannelResponse(channel.getName(), channel.getDescription());
    }
}
