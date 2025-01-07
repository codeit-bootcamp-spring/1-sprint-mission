package com.sprint.mission.discodeit.entity.common;

import static com.sprint.mission.discodeit.entity.common.Status.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

public abstract class AbstractUUIDEntity {

    private final UUID id;

    private final Long createAt;

    protected Long updateAt;

    private Status status;

    protected AbstractUUIDEntity() {
        this.id = UUID.randomUUID();
        this.createAt = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        this.status = CREATED;
    }


    public UUID getId() {
        return id;
    }

    public Long getCreateAt() {
        return createAt;
    }

    public Status getStatus() {
        return status;
    }

    public void modifyStatus() {
        this.status = MODIFY;
    }
}
