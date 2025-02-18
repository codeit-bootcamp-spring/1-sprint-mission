package com.sprint.mission.discodeit.dto.channel;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
public class ChannelUpdateRequestDto {
    private UUID channelId;
    private String NewChannelName;
    private String NewDescription;

    public ChannelUpdateRequestDto(UUID channelId, String newChannelName, String newDescription) {
        this.channelId = channelId;
        NewChannelName = newChannelName;
        NewDescription = newDescription;
    }
}
