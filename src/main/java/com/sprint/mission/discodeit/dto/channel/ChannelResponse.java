package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.Channel;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelResponse(UUID channelId, String channel, Boolean isPrivate, Instant lastMessageTime,
                              List<UUID> userList) {
    public static ChannelResponse fromEntity(Channel channel) {
        return new ChannelResponse(channel.getId(), channel.getChannelName(), channel.isPrivate(), channel.getLastMessageTime(), channel.getUserList());
    }
}
