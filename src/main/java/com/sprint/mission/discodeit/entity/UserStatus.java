package com.sprint.mission.discodeit.entity;

import static com.sprint.mission.discodeit.constant.StringConstant.EMPTY_TIME;
import static com.sprint.mission.discodeit.constant.StringConstant.EMPTY_UUID;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Accessors(fluent = true)
public class UserStatus {

    /**
     * Field: {@code EMPTY_USER_STATUS} is literally empty static UserStatus object
     */
    public static final UserStatus EMPTY_USER_STATUS;
    private final UUID userId;
    private final Instant createAt;
    private final Instant updateAt;
    private final Instant lastAccess;

    static {
        EMPTY_USER_STATUS = new UserStatus(
            UUID.fromString(EMPTY_UUID.getValue()),
            Instant.parse(EMPTY_TIME.getValue()),
            Instant.parse(EMPTY_TIME.getValue()),
            Instant.parse(EMPTY_TIME.getValue())
        );
    }

    public static UserStatus createUserStatus() {
        Instant now = Instant.now();
        return new UserStatus(UUID.randomUUID(), now, now, now);
    }

    public static UserStatus createUserStatus(UUID userId) {
        Instant now = Instant.now();
        return new UserStatus(userId, now, now, now);
    }

    public static UserStatus createUserStatus(UUID userId, Instant createAt, Instant updateAt, Instant lastAccess) {
        return new UserStatus(userId, createAt, updateAt, lastAccess);
    }

    public boolean isOnline() {
        Duration between = Duration.between(lastAccess, Instant.now());
        return between.toMinutes() <= 5;
    }
}
