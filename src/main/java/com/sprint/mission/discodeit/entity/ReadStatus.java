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
public class ReadStatus implements Serializable {
    private static final long serialVersionUID = 1L;

    @Builder.Default
    private UUID id = UUID.randomUUID();

    @Builder.Default
    private Instant createdAt = Instant.now();

    private Instant updatedAt;

    private UUID userId;
    private UUID channelId;
    private Instant lastReadTime;

    // 사용자가 채널을 읽은 마지막 시간 업데이트
    public void updateLastReadTime() {
        this.lastReadTime = Instant.now();
        this.updatedAt = Instant.now();
    }
}
