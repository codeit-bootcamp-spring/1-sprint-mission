package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class ReadStatus implements Serializable, Entity {

    private UUID userId;
    private Instant createdAt;
    @Setter private Instant updatedAt;

    // 생성자
    public ReadStatus(UUID userId) {
        this.userId = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    // 업데이트시간 변경
    public void setUpdatedAt() {
        updatedAt = Instant.now();
    }
}
