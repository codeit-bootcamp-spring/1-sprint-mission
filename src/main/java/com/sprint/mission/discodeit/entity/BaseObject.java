package com.sprint.mission.discodeit.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
@Getter
public abstract class BaseObject implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @JsonProperty("userId")
    private final UUID id;
    @JsonProperty("createdAt")
    private final Instant createdAt;
    @JsonProperty("updatedAt")
    private Instant updatedAt;

    public BaseObject(UUID id, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public BaseObject(UUID id) {
        this(id, Instant.now(), Instant.now());
    }

    public BaseObject() {
        this(UUID.randomUUID(), Instant.now(), Instant.now());
    }

    public BaseObject createBaseObject(UUID id) {
        return this;
    }

    public Instant setUpdatedAt() {
        return Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public Instant getCreatedAtBaseObject() {
        return Instant.now();
    }

    public Instant getUpdatedAtBaseObject() {
        return Instant.now();
    }
}
