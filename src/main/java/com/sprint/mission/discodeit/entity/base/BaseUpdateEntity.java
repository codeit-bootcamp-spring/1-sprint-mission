package com.sprint.mission.discodeit.entity.base;

import jakarta.persistence.Entity;
import jakarta.persistence.MappedSuperclass;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseUpdateEntity extends BaseEntity {

  @LastModifiedDate
  private Instant updatedAt;


}
