package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class ReadStatus implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // 공통 필드
    private final UUID id;          // pk
    private final Instant createAt; // 생성 시간
    private Instant updateAt;       // 수정 시간

    private final UUID userId;      // 사용자 id
    private final UUID channelId;   // 대상 채널 id
    private Instant lastReadTime;   // 마지막으로 메시지를 읽은 시간

    public ReadStatus(UUID userId, UUID channelId) {
        Instant now = Instant.now();    // 객체 생성 시 createAt과 lastReadTime의 차이를 방지하기 위한 변수

        this.id = UUID.randomUUID();
        this.createAt = now;

        this.userId = userId;
        this.channelId = channelId;
        this.lastReadTime = now;
    }

    public void updateUpdateAt() {
        this.updateAt = Instant.now();
    }

    public void updateLastReadTime() {
        this.lastReadTime = Instant.now();
        updateUpdateAt();
    }
}
