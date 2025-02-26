package com.spirnt.mission.discodeit.enity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Getter
public class UserStatus extends Common implements Serializable {
    private static final long serialVersionUID = 1L;
    private UUID userId;
    private UserStatusType userStatusType;
    private Instant lastSeenAt; // 마지막으로 확인된 접속 시간

    public UserStatus(UUID userId, UserStatusType type, Instant lastSeenAt) {
        super();
        this.userId = userId;
        this.userStatusType = type;
        this.lastSeenAt = lastSeenAt;
    }


    public boolean isOnline() {
        if (lastSeenAt == null) {
            return false; // 접속한 적이 없으면 오프라인으로 간주
        }
        Instant fiveMinutesAgo = Instant.now().minus(5, ChronoUnit.MINUTES);
        return lastSeenAt.isAfter(fiveMinutesAgo); // 5분 이내 접속이면 온라인
    }

    public void update(UserStatusType userStatusType, Instant lastSeenAt) {
        boolean anyValueUpdated = false;
        if (lastSeenAt != null && !lastSeenAt.equals(this.lastSeenAt) && this.lastSeenAt.isBefore(lastSeenAt)) {
            this.lastSeenAt = lastSeenAt;
            anyValueUpdated = true;
        }
        if (userStatusType != null && this.userStatusType != userStatusType) {
            this.userStatusType = userStatusType;
            anyValueUpdated = true;
        }
        if (anyValueUpdated) {
            updateClass(Instant.now());
        }
    }

    @Override
    public String toString() {
        return "UserStatus[User: " + this.userId +
                "(" + userStatusType + ")" +
                " LastSeenAt: " + lastSeenAt + "]";
    }

}
