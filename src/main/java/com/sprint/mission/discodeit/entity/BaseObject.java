package com.sprint.mission.discodeit.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

public abstract class BaseObject implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;

    public BaseObject(UUID id, Long createdAt, Long updatedAt) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public BaseObject(UUID id) {
        this(id, System.currentTimeMillis(), System.currentTimeMillis());
    }

    public BaseObject() {
        this(UUID.randomUUID(), System.currentTimeMillis(), System.currentTimeMillis());
    }

    public BaseObject createBaseObject(UUID id) {
        return this;
    }

    public long setUpdatedAt() {
        return this.updatedAt = System.currentTimeMillis();
    }

    public UUID getId() {
        return id;
    }

    public Long getCreatedAtBaseObject() {
        return createdAt;
    }

    public Long getUpdatedAtBaseObject() {
        return updatedAt;
    }
}
