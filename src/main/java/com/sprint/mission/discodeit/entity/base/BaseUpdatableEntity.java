package com.sprint.mission.discodeit.entity.base;

import jakarta.persistence.Column;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public abstract class BaseUpdatableEntity extends BaseEntity {

  @Column(name = "updated_at")
  @LastModifiedDate
  protected Instant updatedAt;
}
