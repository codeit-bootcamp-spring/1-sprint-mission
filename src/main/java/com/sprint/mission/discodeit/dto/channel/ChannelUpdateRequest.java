package com.sprint.mission.discodeit.dto.channel;

import lombok.Getter;

import java.util.UUID;

@Getter
public class ChannelUpdateRequest {
    private UUID channelId;
    private String NewChannelName;
    private String NewDescription;
}
