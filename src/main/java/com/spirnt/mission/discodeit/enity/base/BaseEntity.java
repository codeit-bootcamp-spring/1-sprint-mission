package com.spirnt.mission.discodeit.enity.base;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public abstract class BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(updatable = false)
  private UUID id;
  @CreatedDate
  @Column(updatable = false, name = "created_at")
  private Instant createdAt;

  public BaseEntity() {
    this.createdAt = Instant.now();
  }

}
