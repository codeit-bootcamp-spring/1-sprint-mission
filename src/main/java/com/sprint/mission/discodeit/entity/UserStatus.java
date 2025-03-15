package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import lombok.Getter;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Getter
public class UserStatus extends BaseUpdatableEntity {
    // TODO: userId 변수 및 관련 로직 제거
    private UUID userId;

    private User user;
    private Instant lastActiveAt;

    public UserStatus(User user) {
        this.user = user;
        this.lastActiveAt = Instant.now();
    }

    public void update(Instant lastActiveAt) {
        if (lastActiveAt != null && !lastActiveAt.equals(this.lastActiveAt)) {
            this.lastActiveAt = lastActiveAt;
        }
    }

    public Boolean isOnline() {
        Instant instantFiveMinutesAgo = Instant.now().minus(Duration.ofMinutes(5));

        return lastActiveAt.isAfter(instantFiveMinutesAgo);
    }

    public boolean isSameUserId(UUID userId) {
        return this.userId.equals(userId);
    }
}
