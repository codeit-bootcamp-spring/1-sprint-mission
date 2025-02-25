package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.exception.user.UserStatusNullOrEmptyArgumentException;
import java.io.Serial;
import lombok.AccessLevel;
import lombok.Getter;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class UserStatus implements Serializable {

    @Serial
    private static final long serialVersionUID = -4277133296957649622L;

    private final UUID id;
    private final Instant createdAt;
    private final Instant updatedAt;
    //
    private final UUID userId;
    private final Instant lastActiveAt;

    public static UserStatus createUserStatus(UUID userId, Instant lastActiveAt) {
        return new UserStatus(UUID.randomUUID(), Instant.now(), null, userId, lastActiveAt);
    }

    public UserStatus update(Instant lastActiveAt) {
        if (lastActiveAt == null) {
            throw new UserStatusNullOrEmptyArgumentException("UserStatus lastActiveAt cannot be null");
        }

        if (lastActiveAt.equals(this.lastActiveAt)) {
            return this;
        }

        return new UserStatus(
            this.id,
            this.createdAt,
            Instant.now(),
            this.userId,
            lastActiveAt
        );
    }

    public Boolean isOnline() {
        Instant instantFiveMinutesAgo = Instant.now().minus(Duration.ofMinutes(5));

        return lastActiveAt.isAfter(instantFiveMinutesAgo);
    }
}
