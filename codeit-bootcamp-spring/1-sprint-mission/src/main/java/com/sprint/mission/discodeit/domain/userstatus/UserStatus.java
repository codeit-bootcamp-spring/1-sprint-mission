package com.sprint.mission.discodeit.domain.userstatus;

import com.sprint.mission.discodeit.domain.user.User;
import com.sprint.mission.discodeit.domain.userstatus.enums.OnlineStatus;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.UUID;

public class UserStatus {

    private final UUID id;
    private final User user;
    private final Instant createdAt;
    private Instant lastAccessedAt;

    public UserStatus(User user) {
        validate(user);
        this.id = UUID.randomUUID();
        this.user = user;
        this.createdAt = Instant.now();
        this.lastAccessedAt = Instant.now();
    }

    private void validate(User user) {
        if (Objects.isNull(user)) {
            throw new IllegalArgumentException("user is null");
        }
    }

    public void updateLastAccessedAt() {
        this.lastAccessedAt = Instant.now();
    }

    public OnlineStatus getOnlineStatus() {
        if (lastAccessedAt.isAfter(Instant.now().minus(5, ChronoUnit.MINUTES))) {
            return OnlineStatus.ONLINE;
        }
        return OnlineStatus.OFFLINE;
    }

    public User getUser() {
        return user;
    }
}
