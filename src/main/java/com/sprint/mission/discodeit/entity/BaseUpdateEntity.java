package com.sprint.mission.discodeit.entity;

import jakarta.persistence.MappedSuperclass;
import java.time.Instant;

@MappedSuperclass
public abstract class BaseUpdateEntity extends BaseEntity {

  Instant updatedAt;
}
