package com.sprint.mission.discodeit.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public abstract class ImmutableBaseObject implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull
    @JsonProperty("userId")
    private final UUID id;

    @JsonProperty("createdAt")
    private final Instant createdAt;

    public ImmutableBaseObject() {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
    }
}
