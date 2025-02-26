package com.spirnt.mission.discodeit.dto.userStatus;

import com.spirnt.mission.discodeit.enity.UserStatusType;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class UserStatusResponse {
    private UUID userId;
    private UserStatusType type;
    private Instant lastSeenAt;

    public UserStatusResponse(UUID userId, UserStatusType type, Instant lastSeenAt) {
        this.userId = userId;
        this.type = type;
        this.lastSeenAt = lastSeenAt;
    }
}
