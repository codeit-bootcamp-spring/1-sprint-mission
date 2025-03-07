package com.sprint.mission.discodeit.entity.base;


import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

import java.time.Instant;

@MappedSuperclass
@Getter
public abstract class BaseUpdatableEntity  extends BaseEntity{

    @Column(name = "updated_at")
    private Instant updatedAt;

    public BaseUpdatableEntity() {
        super();
        this.updatedAt = Instant.now();
    }
}
