package com.sprint.mission.discodeit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;

import java.time.Instant;

@MappedSuperclass
@Getter
public abstract class BaseUpdatableEntity extends BaseEntity{

    @Column(name="updated_at", nullable = false)
    private Instant updatedAt = Instant.ofEpochMilli(System.currentTimeMillis());


    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.ofEpochMilli(System.currentTimeMillis());
    }
}
