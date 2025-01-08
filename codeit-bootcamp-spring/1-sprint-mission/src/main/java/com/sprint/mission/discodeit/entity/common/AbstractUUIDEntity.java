package com.sprint.mission.discodeit.entity.common;

import static com.sprint.mission.discodeit.entity.common.Status.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

public abstract class AbstractUUIDEntity {

    private final UUID id;

    private final Long createAt;

    protected Long updateAt = null;

    private Status status;

    protected AbstractUUIDEntity() {
        this.id = UUID.randomUUID();
        this.createAt = createUnixTimestamp();
        this.status = CREATED;
    }

    public UUID getId() {
        return id;
    }

    public Long getCreateAt() {
        return createAt;
    }

    public Optional<Long> getUpdateAt() {
        return Optional.ofNullable(updateAt);
    }

    public Status getStatus() {
        return status;
    }

    public long update() {
        this.status = MODIFY;
        return this.updateAt = createUnixTimestamp();
    }

    private long createUnixTimestamp() {
        return LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
}
