package com.sprint.mission.discodeit.entity.channel;

import com.sprint.mission.discodeit.entity.user.User;
import lombok.Getter;

import java.util.UUID;

@Getter
public class ChannelPrivateRequest {
    private final UUID channelId;
    private String channelName;
    private User owner;
    private String channelType;

    public ChannelPrivateRequest(UUID channelId, String channelName, User owner, String channelType) {
        this.channelId = channelId;
        this.channelName = channelName;
        this.owner = owner;
        this.channelType = channelType;
    }
}
