package com.spirnt.mission.discodeit.dto.channel;

import lombok.Getter;

@Getter
public class ChannelUpdateRequest {
    private String name;
    private String description;

    public ChannelUpdateRequest(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
