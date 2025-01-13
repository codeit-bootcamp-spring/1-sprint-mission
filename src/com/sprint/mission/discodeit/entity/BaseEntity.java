package com.sprint.mission.discodeit.entity;

import java.util.Objects;
import java.util.UUID;

public abstract class BaseEntity {
    private final UUID id;
    private final Long createAt;
    private final Long updateAt;

    protected BaseEntity(UUID id, Long createAt, Long updateAt) {
        this.id       = id;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }

    public UUID getId() {
        return id;
    }
    public Long getCreateAt() {
        return createAt;
    }
    public Long getUpdateAt() {
        return updateAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BaseEntity that = (BaseEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "BaseEntity{" +
                "id=" + id +
                ", createAt=" + createAt +
                ", updateAt=" + updateAt +
                '}';
    }
}
