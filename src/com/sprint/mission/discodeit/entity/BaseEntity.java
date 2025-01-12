package com.sprint.mission.discodeit.entity;

import java.util.Objects;
import java.util.UUID;

public class BaseEntity {
    protected static final BaseEntity EMPTY_BASE_ENTITY = new BaseEntity(
            new UUID(0, 0),
            0L,
            0L
    );

    private final UUID id;
    private final Long createAt;
    private final Long updateAt;

    protected BaseEntity() {
        this(UUID.randomUUID(), System.currentTimeMillis(), System.currentTimeMillis());
    }
    protected BaseEntity(UUID id) {
        this(id, System.currentTimeMillis(), System.currentTimeMillis());
    }
    protected BaseEntity(UUID id, Long createAt) {
        this(id, createAt, System.currentTimeMillis());
    }
    protected BaseEntity(UUID id, Long createAt, Long updateAt) {
        this.id       = id;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }
    protected BaseEntity(BaseEntity baseEntity) {
        this.id = baseEntity.id;
        this.createAt = baseEntity.createAt;
        this.updateAt = baseEntity.updateAt;
    }

    public static BaseEntity createBaseEntity() {
        return new BaseEntity();
    }
    public static BaseEntity createBaseEntity(UUID id) {
        return new BaseEntity(id);
    }
    public static BaseEntity createBaseEntity(UUID id, Long createAt) {
        return new BaseEntity(id, createAt);
    }
    public static BaseEntity createEmptyBaseEntity() {
        return EMPTY_BASE_ENTITY;
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
