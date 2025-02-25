package com.sprint.mission.discodeit.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
public class ReadStatus implements Serializable { // 사용자가 채널 별 마지막 메시지를 읽은 시간을 표현하는 도메인 모델
    @Serial
    private static final long serialVersionUID = 1L;
    private UUID id;
    private Instant createdAt;
    private Instant updateAt;
    private UUID userId;
    private UUID channelId;
    private Instant lastReadAt;

    public ReadStatus(UUID userId, UUID channelId, Instant lastReadAt) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updateAt = Instant.now();
        this.userId = userId;
        this.channelId = channelId;
        this.lastReadAt = lastReadAt;
    }

    public void updateLastReadTime(Instant lastReadAt) {
        this.updateAt = Instant.now();
        this.lastReadAt = lastReadAt;
    }
}
