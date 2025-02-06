package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private UUID id;
    private Instant createdAt;
    private Instant updatedAt;

    public BaseEntity() {
        this.id = UUID.randomUUID();
        this.createdAt = getCurrentTime();
        this.updatedAt = createdAt;
    }

    protected Instant getCurrentTime() {
        return Instant.now();
    }

    public void update(){
        this.updatedAt = getCurrentTime();
    }
}
