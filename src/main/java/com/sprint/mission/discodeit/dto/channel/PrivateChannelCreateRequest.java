package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.ReadStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class PrivateChannelCreateRequest {
    private final UUID userId;
    private final List<UUID> members;

    public PrivateChannelCreateRequest(UUID userId, List<UUID> members) {
        this.userId = userId;
        this.members = members;
    }
}
