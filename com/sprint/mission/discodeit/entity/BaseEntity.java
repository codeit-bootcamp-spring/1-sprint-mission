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
    private BaseEntity(UUID id, Long createAt, Long updateAt) {
        this.id       = id;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }

    public static BaseEntity createBasicEntity() {
        return new BaseEntity();
    }
    public static BaseEntity createBasicEntity(UUID id) {
        return new BaseEntity(id);
    }
    public static BaseEntity createEmptyBasicEntity() {
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
