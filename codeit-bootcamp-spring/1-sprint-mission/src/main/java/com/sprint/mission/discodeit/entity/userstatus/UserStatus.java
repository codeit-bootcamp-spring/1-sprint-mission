package com.sprint.mission.discodeit.entity.userstatus;

import com.sprint.mission.discodeit.entity.user.entity.User;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.UUID;
import lombok.Getter;

@Getter
public class UserStatus {

    private final UUID id;
    private Instant createdAt;
    private Instant updatedAt;
    private final User user;

    public UserStatus(User user) {
        Objects.requireNonNull(user, "user is null");
        this.id = UUID.randomUUID();
        this.user = user;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }
    public void updateConnectTime() {
        updatedAt = Instant.now();
    }
    public boolean isActiveWithInFiveMinutes() {
        Instant withInFiveMin = Instant.now().minus(5, ChronoUnit.MINUTES);
        return updatedAt.isAfter(withInFiveMin);
    }
}
