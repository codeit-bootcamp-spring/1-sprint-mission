package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.util.time.SystemTimeProvider;
import com.sprint.mission.discodeit.util.time.TimeProvider;

import java.util.UUID;

public abstract class BaseEntity{
    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;
    private final TimeProvider timeProvider;

    public BaseEntity(){
        this(new SystemTimeProvider());
    }

    public BaseEntity(TimeProvider timeProvider) {
        this.timeProvider = timeProvider;
        this.id = UUID.randomUUID();
        this.createdAt = timeProvider.getCurrentTimeMillis();
        this.updatedAt = this.createdAt;
    }

    public UUID getId() {
        return id;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void updateTimeStamp() {
        updatedAt = timeProvider.getCurrentTimeMillis();
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null) return false;
        if(getClass() != o.getClass()) return false; // 엔티티 타입 매칭
        BaseEntity that = (BaseEntity) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode(){
        return id.hashCode();
    }
}