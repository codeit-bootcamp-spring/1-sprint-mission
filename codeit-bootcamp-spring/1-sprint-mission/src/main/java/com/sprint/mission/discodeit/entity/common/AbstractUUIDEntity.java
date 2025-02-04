package com.sprint.mission.discodeit.entity.common;

import static com.sprint.mission.discodeit.entity.common.Status.MODIFIED;
import static com.sprint.mission.discodeit.entity.common.Status.REGISTERED;
import static com.sprint.mission.discodeit.entity.common.Status.UNREGISTERED;

import com.google.common.base.Preconditions;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;

@Getter
public abstract class AbstractUUIDEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -2898060967687082469L;
    private final UUID id;

    private final Instant createAt;

    private Instant updateAt;

    private Status status;

    protected AbstractUUIDEntity() {
        this.id = UUID.randomUUID();
        this.createAt = createUnixTimestamp();
        this.updateAt = createUnixTimestamp();
        this.status = REGISTERED;
    }

    private void updateStatus(Status status) {
        Preconditions.checkNotNull(status);
        this.status = status;
        this.updateAt = createUnixTimestamp();
    }

    public void updateStatusAndUpdateAt() {
        updateStatus(MODIFIED);
    }

    public void updateUnregistered() {
        updateStatus(UNREGISTERED);
    }

    public boolean isNotUnregistered() {
        return status != UNREGISTERED;
    }

    private Instant createUnixTimestamp() {
        return Instant.now();
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
