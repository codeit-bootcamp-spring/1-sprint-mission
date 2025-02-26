package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.ChannelType;
import lombok.Getter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
public class ChannelDto {
    private  UUID channelId;
    private  String channelName;
    private  ChannelType type;
    private  String description;
    private  Instant lastMessageAt;
    private  List<UUID> members;

    public ChannelDto(UUID channelId, String channelName, ChannelType type, String description, Instant lastMessageAt, List<UUID> members) {
        this.channelId = channelId;
        this.channelName = channelName;
        this.type = type;
        this.description = description;
        this.lastMessageAt = lastMessageAt;
        this.members = members;
    }
}
