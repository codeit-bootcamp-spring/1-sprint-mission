package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import lombok.Getter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
public class ChannelResponseDto {
    private final UUID channelId;
    private final String channelName;
    private final ChannelType type;
    private final Instant lastMessageTime;
    private final List<UUID> members;

    public ChannelResponseDto(Channel channel,Instant lastMessageTime, List<UUID> memberIds) {
        this.channelId = channel.getId();
        this.channelName = channel.getChannelName();
        this.type = channel.getType();
        this.lastMessageTime = lastMessageTime;
        this.members = memberIds;
    }
}
