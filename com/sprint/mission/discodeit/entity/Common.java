package sprint.mission.discodeit.entity;

import java.util.Objects;
import java.util.UUID;

public class Common {
    private final UUID id;
    private final Long createAt;
    private final Long updateAt;

    private Common() {
        this(UUID.randomUUID(), System.currentTimeMillis(), System.currentTimeMillis());
    }
    private Common(UUID id) {
        this(id, System.currentTimeMillis(), System.currentTimeMillis());
    }
    private Common(UUID id, Long createAt) {
        this(id, createAt, System.currentTimeMillis());
    }
    private Common(Long createAt, Long updateAt) {
        this(UUID.randomUUID(), createAt, updateAt);
    }
    private Common(UUID id, Long createAt, Long updateAt) {
        this.id = id;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }

    public static Common createCommon() {
        return new Common();
    }
    public static Common createCommon(UUID id) {
        return new Common(id);
    }
    public static Common createEmptyCommon() {
        return new Common(
                new UUID(0, 0),
                0L,
                0L
        );
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

    public Common updateId(Common common) {
        return new Common(common.createAt, common.updateAt);
    }

    public Common updateUpdateAt(Common common) {
        return new Common(common.id, common.createAt);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Common common = (Common) o;
        return Objects.equals(id, common.id) && Objects.equals(createAt, common.createAt) && Objects.equals(updateAt, common.updateAt);
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
