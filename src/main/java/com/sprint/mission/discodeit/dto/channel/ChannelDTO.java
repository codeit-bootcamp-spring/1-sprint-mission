package com.sprint.mission.discodeit.dto.channel;

import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class ChannelDTO extends ChannelBaseDTO {
    private List<UUID> userId;
    public ChannelDTO(String channelName, String description, List<UUID> users) {
        super(channelName,description);
        this.userId = users;
    }
}
