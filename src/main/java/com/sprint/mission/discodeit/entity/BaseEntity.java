package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import java.time.Instant;
import java.util.UUID;

@Getter
public abstract class BaseEntity {
  private final UUID id;
  private final Long createdAt;
  private Long updatedAt;
  
  public BaseEntity() {
    this.id = UUID.randomUUID();
    this.createdAt = Instant.now().getEpochSecond();
    this.updatedAt = null;
  }
  
  public void update() {
    this.updatedAt = Instant.now().getEpochSecond();
  }
  
}
