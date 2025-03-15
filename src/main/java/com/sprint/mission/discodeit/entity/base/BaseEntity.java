package com.sprint.mission.discodeit.entity.base;

import jakarta.persistence.EntityListeners;
import java.time.Instant;
import java.util.UUID;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

  private UUID id;

  @CreatedDate
  private Instant createdAt;
}
