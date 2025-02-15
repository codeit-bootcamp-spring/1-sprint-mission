package com.sprint.mission.discodeit.entity;

import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.UUID;

public class ReadStatus {

    // 사용자가 채널 별 마지막으로 메시지를 읽은 시간을 표현하는 도메인 모델.
    // 사용자별 각 채널에 읽지 않은 메시지를 확인하기 위해 활용됨.
    private UUID id;
    private Instant createdAt;
    private Instant updatedAt;
    private UUID userId;
    private UUID channelId;
    private Instant lastReadAt;

    public ReadStatus(UUID userId, UUID channelId, Instant lastReadAt) {
        this.userId = userId;
        this.channelId = channelId;
        this.lastReadAt = lastReadAt;

        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
    }

    public void update(Instant updateReadAt) {
        if (updateReadAt!= null && !updateReadAt.equals(lastReadAt)) {
            lastReadAt = updateReadAt;
            updatedAt = Instant.now();
        }
    }
}
