package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public abstract class BaseEntity implements Serializable {
   private final UUID id;
   private final Instant createdAt;
   private Instant updatedAt;

    public BaseEntity() {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.ofEpochSecond(System.currentTimeMillis());
        this.updatedAt = createdAt;
    }

    public void setUpdatedAt() {
        this.updatedAt= Instant.ofEpochSecond(System.currentTimeMillis());
    }
}
