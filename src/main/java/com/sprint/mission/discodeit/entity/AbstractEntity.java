package com.sprint.mission.discodeit.entity;
import java.util.UUID;

public abstract class AbstractEntity {
    protected final UUID id;
    protected final Long createAt;
    protected Long updateAt;

    public AbstractEntity() {
        this.id = UUID.randomUUID();
        this.createAt = System.currentTimeMillis();
        this.updateAt = this.createAt;
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
    protected void updateTimestamp(){
        this.updateAt = System.currentTimeMillis();
    }

}
