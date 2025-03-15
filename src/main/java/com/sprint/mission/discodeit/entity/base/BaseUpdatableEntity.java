package com.sprint.mission.discodeit.entity.base;

import jakarta.persistence.EntityListeners;
import java.time.Instant;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
public abstract class BaseUpdatableEntity extends BaseEntity {

  @LastModifiedDate
  private Instant updatedAt;
}
