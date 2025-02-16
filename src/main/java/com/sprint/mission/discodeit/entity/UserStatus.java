package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

import static com.sprint.mission.discodeit.entity.Status.CONNECTED;

@Getter
public class UserStatus implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;
    private UUID userId;
    private Status status;


    public UserStatus(UUID userId) {
        this.userId = userId;
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now().toEpochMilli();
        this.updatedAt = createdAt;
        this.status = CONNECTED;
    }

    public void updateStatus(Status status){
        this.status = status;
    }
}

