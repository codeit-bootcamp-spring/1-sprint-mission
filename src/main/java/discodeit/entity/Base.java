package discodeit.entity;

import java.util.UUID;

public abstract class Base {
    private UUID id;
    private Long createdAt;
    private Long updatedAt;

    // 생성자에서 id, createdAt 초기화
    public Base() {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
    }

    // Getter
    public UUID getId() {
        return id;
    }
    public Long getCreatedAt() {
        return createdAt;
    }
    public Long getUpdatedAt() {
        return updatedAt;
    }

    // Update
    public void updateId(UUID id) {
        this.id = id;
    }
    public void updateCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }
    public void updateUpdatedAt() {
        this.updatedAt = System.currentTimeMillis();
    }
}
