package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.global.error.ErrorCode;
import com.sprint.mission.discodeit.global.util.TimeUtil;
import lombok.Getter;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Getter
public class UserStatus extends BaseEntity {

    private final User user;
    private Instant loginAt;
    private boolean isOnline = false;

    private static final long LOGIN_EXPIRATION_MINUTES = 5;

    private UserStatus(User user) {
        super();
        this.user = user;
        this.loginAt = this.getCreatedAt();
    }

    public static UserStatus of(User user) {
        if (user == null) {
            throw new IllegalArgumentException(ErrorCode.USER_NOT_NULL.getMessage());
        }
        return new UserStatus(user);
    }

    public boolean isOnline() {
        return this.isOnline;
    }

    public void updateLoginAt() {
        this.loginAt = TimeUtil.getCurrentTime();
        this.updateUpdatedAt();
    }

    public boolean isRecentLogin() {
        return ChronoUnit.MINUTES.between(Instant.now(), this.loginAt) < LOGIN_EXPIRATION_MINUTES;
    }

    public void updateOnline(boolean isOnline) {
        this.isOnline = isOnline;
        this.updateUpdatedAt();
    }

    public void updateUserStatusInfo(boolean isOnline) {
        this.updateLoginAt();
        this.updateOnline(isOnline);
    }
}
