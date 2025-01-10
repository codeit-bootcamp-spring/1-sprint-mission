package com.sprint.mission.discodeit.entity.common;

import static com.sprint.mission.discodeit.entity.common.Status.MODIFIED;
import static com.sprint.mission.discodeit.entity.common.Status.REGISTERED;
import static com.sprint.mission.discodeit.entity.common.Status.UNREGISTERED;

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
        this.status = REGISTERED;
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

    private void updateStatus(Status status) {
        this.status = status;
        this.updateAt = createUnixTimestamp();
    }

    public void updateStatusAndUpdateAt() {
        updateStatus(MODIFIED);
    }

    public void updateUnregistered() {
        updateStatus(UNREGISTERED);
    }

    private long createUnixTimestamp() {
        return LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AbstractUUIDEntity that = (AbstractUUIDEntity) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
