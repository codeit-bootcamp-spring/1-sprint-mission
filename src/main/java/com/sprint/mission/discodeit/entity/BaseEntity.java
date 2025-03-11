package com.sprint.mission.discodeit.entity;

import jakarta.persistence.*;
import lombok.Getter;


import java.time.Instant;
import java.util.UUID;


@MappedSuperclass
@Getter
public abstract class BaseEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name="created_at", updatable = false, nullable = false)
    private Instant createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.ofEpochMilli(System.currentTimeMillis());
    }

}