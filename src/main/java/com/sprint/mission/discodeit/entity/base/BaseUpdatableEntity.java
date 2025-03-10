package com.sprint.mission.discodeit.entity.base;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;

@MappedSuperclass
@Getter
@Setter
public class BaseUpdatableEntity extends BaseEntity {

    @LastModifiedDate
    @Column(nullable = true)
    private Instant updatedAt;

    public void setUpdatedAtNow() {
        this.updatedAt = Instant.now();
    }
}
