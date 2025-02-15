package com.sprint.mission.discodeit.dto.channel;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public abstract class ChannelBaseDTO {
    private String channelName;
    private String description;

    public ChannelBaseDTO(String channelName, String description) {
        this.channelName = channelName;
        this.description = description;
    }
}
