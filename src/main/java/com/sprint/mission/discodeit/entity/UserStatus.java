package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserStatus implements Serializable {
    private static final long serialVersionUID = 1L;

    @Builder.Default
    private UUID id = UUID.randomUUID();

    @Builder.Default
    private Instant createdAt = Instant.now();

    private Instant updatedAt;

    private UUID userId;
    private Instant lastSeenTime;

    // 마지막 접속 시간을 기준으로 현재 접속 중인 유저 판단
    public boolean isOnline() {
        return lastSeenTime.isAfter(Instant.now().minusSeconds(5 * 60)); // 5분 이내 접속이면 온라인으로 간주
    }
}
