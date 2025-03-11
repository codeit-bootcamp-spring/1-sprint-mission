package com.sprint.mission.discodeit.entity.base;

import jakarta.persistence.EntityListeners;
import java.time.Instant;
import lombok.Getter;
import org.springframework.data.annotation.LastModifiedDate;
@Getter
public abstract class BaseUpdateableEntity extends BaseEntity {
  @LastModifiedDate
  protected Instant updatedAt;
}
