package com.sprint.mission.discodeit.dto.channel;

import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class PrivateChannelCreateRequestDto {
    private final UUID userId;
    private final List<UUID> members;

    public PrivateChannelCreateRequestDto(UUID userId, List<UUID> members) {
        this.userId = userId;
        this.members = members;
    }
}
