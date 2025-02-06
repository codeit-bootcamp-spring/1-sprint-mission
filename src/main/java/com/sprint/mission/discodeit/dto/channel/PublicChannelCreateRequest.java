package com.sprint.mission.discodeit.dto.channel;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
public class PublicChannelCreateRequest {
    private final UUID userId;
    private final String channelName;
    private final String description;

    public PublicChannelCreateRequest(UUID userId, String channelName, String description) {
        this.userId = userId;
        this.channelName = channelName;
        this.description = description;
    }
}
