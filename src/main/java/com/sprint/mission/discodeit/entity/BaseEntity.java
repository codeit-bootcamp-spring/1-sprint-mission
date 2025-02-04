package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.util.time.SystemTimeProvider;
import com.sprint.mission.discodeit.util.time.TimeProvider;
import lombok.Getter;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;


public abstract class BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 10L;
    @Getter
    private final UUID id;
    @Getter
    private final Instant createdAt;
    @Getter
    private Instant updatedAt;
    private transient TimeProvider timeProvider;

    public BaseEntity(){
        this(new SystemTimeProvider());
    }

    public BaseEntity(TimeProvider timeProvider) {
        this.timeProvider = timeProvider;
        this.id = UUID.randomUUID();
        this.createdAt = timeProvider.getCurrentTime();
        this.updatedAt = this.createdAt;
    }

    public void updateTimeStamp() {
        updatedAt = timeProvider.getCurrentTime();
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

    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.timeProvider = new SystemTimeProvider();
    }
}