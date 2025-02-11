package com.sprint.mission.discodeit.entity;

import java.io.Serial;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

public class UserStatus extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 16L;

    /*
    사용자 별 마지막으로 확인된 접속 시간을 표현하는 도메인 모델
    사용자의 온라인 상태를 확인하기 위해 활용
     */
    private UUID userId;

    public UserStatus(UUID userId) {
        super();
        this.userId = userId;
    }

    public void update() {
        updateTimeStamp();
    }

    //5분이내
    private boolean isOnline() {
        return getUpdatedAt().isAfter(Instant.now().minus(Duration.ofMinutes(5)));
    }

    //5~10분이내
    private boolean isIdle() {
        return getUpdatedAt().isAfter(Instant.now().minus(Duration.ofMinutes(10)));
    }

    public UserStatusType getStatus() {
        if (isOnline()) {
            return UserStatusType.ONLINE;
        } else if (isIdle()) {
            return UserStatusType.IDLE;
        } else {
            return UserStatusType.OFFLINE;
        }
    }
}
