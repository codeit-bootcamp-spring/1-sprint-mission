package discodeit.entity;

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
    private Common(Long updateAt) {
        this(UUID.randomUUID(), System.currentTimeMillis(), updateAt);
    }
    private Common(UUID id, Long createAt, Long updateAt) {
        this.id = id;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }

    public static Common createCommon() {
        return new Common();
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

    public Common updateId(UUID id) {
        return new Common(id);
    }

    public Common updateUpdateAt(Long updateAt) {
        return new Common(updateAt);
    }
}
