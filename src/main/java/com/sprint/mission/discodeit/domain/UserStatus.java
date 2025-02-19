package com.sprint.mission.discodeit.domain;

import com.sprint.mission.discodeit.util.type.OnlineStatusType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Getter
@Setter
@ToString
public class UserStatus implements Serializable { // 사용자 별 마지막으로 확인된 접속 시간을 표현하는 도메인 모델
    @Serial
    private static final long serialVersionUID = 1L;
    private final UUID id;
    private Instant createdAt;
    private Instant updateAt;
    private Instant lastActiveAt;
    private UUID userId;
    private OnlineStatusType onlineStatusType;

    public UserStatus(UUID userId, Instant lastActiveAt) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updateAt = Instant.now();
        this.lastActiveAt = lastActiveAt;
        this.userId = userId;
        this.onlineStatusType = isOnline();
    }

    public OnlineStatusType isOnline() {
        return ChronoUnit.MINUTES.between(lastActiveAt, Instant.now()) <= 5 ? OnlineStatusType.ACTIVE : OnlineStatusType.SLEEP;
    }

    public void update(Instant lastActiveAt) {
        this.updateAt = Instant.now();
        this.lastActiveAt = lastActiveAt;
        this.onlineStatusType = isOnline();
    }
}
