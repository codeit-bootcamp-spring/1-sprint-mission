package com.sprint.mission.discodeit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PreUpdate;
import java.time.Instant;
import lombok.Getter;
import org.springframework.data.annotation.LastModifiedDate;

@Getter
@MappedSuperclass
public abstract class BaseUpdatableEntity extends BaseEntity {

  @Column(nullable = true)
  @LastModifiedDate
  private Instant updatedAt;

  @PreUpdate
  protected void onUpdate() {
    this.updatedAt = Instant.now();
  }
}
