package com.spirnt.mission.discodeit.enity.base;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.Instant;
import lombok.Getter;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public abstract class BaseUpdatableEntity extends BaseEntity {

  @LastModifiedDate
  @Column(updatable = true, name = "updated_at")
  private Instant updatedAt;

  public BaseUpdatableEntity() {
    super();
    this.updatedAt = Instant.now();
  }
}
