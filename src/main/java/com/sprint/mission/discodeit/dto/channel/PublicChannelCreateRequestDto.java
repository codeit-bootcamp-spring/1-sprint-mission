package com.sprint.mission.discodeit.dto.channel;

import lombok.Getter;

import java.util.UUID;

@Getter
public class PublicChannelCreateRequestDto {
    private final UUID userId;
    private final String channelName;
    private final String description;

    public PublicChannelCreateRequestDto(UUID userId, String channelName, String description) {
        this.userId = userId;
        this.channelName = channelName;
        this.description = description;
    }
}
