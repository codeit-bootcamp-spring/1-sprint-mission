package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Getter
public class UserStatus implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // 공통 필드
    private final UUID id;          // pk
    private final Instant createAt; // 생성 시간
    private Instant updateAt;       // 수정 시간

    private UUID userId;
    private Instant lastAccessTime; // 최종 접속 시간

    public UserStatus(UUID userId) {
        Instant now = Instant.now();

        this.id = UUID.randomUUID();
        this.createAt = now;

        this.userId = userId;
        this.lastAccessTime = now;
    }

    public void updateUpdateAt() {
        this.updateAt = Instant.now();
    }

    public void updateLastAccessTime() {
        this.lastAccessTime = Instant.now();
        updateUpdateAt();
    }

    // 현재 유저가 접속해있는지 판별하는 메서드
    // 현재 시간 기준으로 마지막 접속으로부터 5분 이내이면 접속 중으로 판단
    public boolean checkAccess() {
        Instant now = Instant.now();

        Duration between = Duration.between(lastAccessTime, now);

        return between.getSeconds() <= 300;     // 접속 중이면 true, 아니면 false 반환
    }
}
