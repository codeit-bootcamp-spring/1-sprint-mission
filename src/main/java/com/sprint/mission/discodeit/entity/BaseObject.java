package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public abstract class BaseObject {
    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;

    public BaseObject createBaseObject(UUID id) {
        return this;
    }

    public BaseObject(UUID id, Long createdAt, Long updatedAt) {
        if(id != null) {
            this.id = id;
        }
        else {
            this.id = UUID.randomUUID();
        }
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public BaseObject(UUID id) {
        this(id, System.currentTimeMillis(), System.currentTimeMillis());
    }

    public BaseObject() {
        this(UUID.randomUUID(), System.currentTimeMillis(), System.currentTimeMillis());
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
