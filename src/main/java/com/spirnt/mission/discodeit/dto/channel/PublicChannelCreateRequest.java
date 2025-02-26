package com.spirnt.mission.discodeit.dto.channel;

import lombok.Getter;

import java.util.UUID;

@Getter
public class PublicChannelCreateRequest {
    private String name;
    private String description;
    private UUID userId;    // 채널을 만든 사람

    public PublicChannelCreateRequest(String name, String description, UUID userId) {
        this.name = name;
        this.description = description;
        this.userId = userId;
    }
}
