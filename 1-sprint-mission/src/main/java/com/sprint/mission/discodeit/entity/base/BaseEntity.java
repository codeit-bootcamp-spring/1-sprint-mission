package com.sprint.mission.discodeit.entity.base;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;


@MappedSuperclass
@Getter
public abstract class BaseEntity {

  @Id
  @Column(name = "id")
  public UUID id;

  @Column(name = "created_at")
  @CreatedDate
  public Instant createdAt;

}
