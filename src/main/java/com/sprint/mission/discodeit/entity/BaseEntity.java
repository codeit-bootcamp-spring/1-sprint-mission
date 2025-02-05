package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.util.Identifiable;
import java.util.UUID;

public abstract class BaseEntity implements Identifiable {
  private final UUID id;
  private final Long createdAt;
  private Long updatedAt;
  
  public BaseEntity() {
    this.id = UUID.randomUUID();
    this.createdAt = System.currentTimeMillis();
    this.updatedAt = null;
  }
  
  public void update() {
    this.updatedAt = System.currentTimeMillis();
  }
  
  @Override
  public UUID getId() {
    return id;
  }
  
  public Long getCreatedAt() {
    return createdAt;
  }
  
  public Long getUpdatedAt() {
    return updatedAt;
  }
  
}
