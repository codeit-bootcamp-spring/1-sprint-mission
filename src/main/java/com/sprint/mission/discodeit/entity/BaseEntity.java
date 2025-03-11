package com.sprint.mission.discodeit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;

@Getter
@MappedSuperclass
public abstract class BaseEntity {

  @Id
  private UUID id;

  @Column(nullable = false, updatable = false)
  @CreatedDate
  private Instant createdAt;

  @PrePersist
  protected void onCreate() {
    this.id = UUID.randomUUID();
    this.createdAt = Instant.now();
  }
}
