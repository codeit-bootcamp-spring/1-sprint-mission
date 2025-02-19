package com.sprint.mission.discodeit.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
public abstract class Channel implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final UUID id;
    private Instant createdAt;
    private Instant updatedAt;
    private List<UUID> joinMembers;

    public Channel(List<UUID> joinMembers) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.joinMembers = joinMembers;
    }
}
