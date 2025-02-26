package com.sprint.mission.discodeit.dto.channel;

import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class PrivateChannelCreateRequestDto {
    private final List<UUID> members;
    private UUID userId;

    public PrivateChannelCreateRequestDto(List<UUID> members) {
        this.members = members;
    }
}
