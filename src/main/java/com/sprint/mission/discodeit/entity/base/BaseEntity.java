package com.sprint.mission.discodeit.entity.base;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@MappedSuperclass
@Getter
@SuperBuilder
public abstract class BaseEntity {

  protected BaseEntity() {
  }

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(columnDefinition = "uuid", updatable = false, nullable = false)
  private UUID id;

  @CreationTimestamp
  @Column(name = "created_at")
  private Instant createdAt;

  // test용
  public void setId(UUID id) {
    this.id = id;
  }
}
