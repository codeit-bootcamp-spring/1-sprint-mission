package com.sprint.mission.discodeit.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import java.time.Instant;
import java.util.UUID;

@MappedSuperclass
@Getter
public abstract class BaseEntity {
  
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;
  private Long createdAt;
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
