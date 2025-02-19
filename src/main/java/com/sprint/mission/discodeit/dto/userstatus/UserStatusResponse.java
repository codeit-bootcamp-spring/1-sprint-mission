package com.sprint.mission.discodeit.dto.userstatus;

import com.sprint.mission.discodeit.entity.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserStatusResponse {
    private UUID id;
    private UUID userId;
    private Instant lastSeenAt;
    private Instant updatedAt;

    public UserStatusResponse(UserStatus userStatus) {
        this.id = userStatus.getId();
        this.userId = userStatus.getUser().getId();
        this.lastSeenAt = userStatus.getLastSeenAt();
        this.updatedAt = userStatus.getUpdatedAt();
    }
}
