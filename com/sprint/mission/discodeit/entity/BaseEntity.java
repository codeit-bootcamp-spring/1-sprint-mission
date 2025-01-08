package sprint.mission.discodeit.entity;

import java.util.Objects;
import java.util.UUID;

public class BaseEntity {
    private static final BaseEntity EMPTY_BASE_ENTITY = new BaseEntity(
            new UUID(0, 0),
            0L,
            0L
    );

    private final UUID id;
    private final Long createAt;
    private final Long updateAt;

    private BaseEntity() {
        this(UUID.randomUUID(), System.currentTimeMillis(), System.currentTimeMillis());
    }
    private BaseEntity(UUID id) {
        this(id, System.currentTimeMillis(), System.currentTimeMillis());
    }
    private BaseEntity(UUID id, Long createAt) {
        this(id, createAt, System.currentTimeMillis());
    }
    private BaseEntity(Long createAt, Long updateAt) {
        this(UUID.randomUUID(), createAt, updateAt);
    }
    private BaseEntity(UUID id, Long createAt, Long updateAt) {
        this.id       = id;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }

    public static BaseEntity createCommon() {
        return new BaseEntity();
    }
    public static BaseEntity createCommon(UUID id) {
        return new BaseEntity(id);
    }
    public static BaseEntity createEmptyCommon() {
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

    public BaseEntity updateId(BaseEntity baseEntity) {
        return new BaseEntity(baseEntity.createAt, baseEntity.updateAt);
    }

    public BaseEntity updateUpdateAt(BaseEntity baseEntity) {
        return new BaseEntity(baseEntity.id, baseEntity.createAt);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BaseEntity baseEntity = (BaseEntity) o;
        return Objects.equals(id, baseEntity.id) && Objects.equals(createAt, baseEntity.createAt) && Objects.equals(updateAt, baseEntity.updateAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Common{" +
                "id=" + id +
                ", createAt=" + createAt +
                ", updateAt=" + updateAt +
                '}';
    }
}
