package com.sprint.mission.discodeit.entity;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.Instant;
import lombok.Getter;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public abstract class BaseUpdatableEntity extends Base {

  private static final long serialVersionUID = 1L; //직렬화 버전

  @LastModifiedDate
  private Instant updatedAt;

  public BaseUpdatableEntity() {
    super();
    this.updatedAt = super.getCreatedAt();
  }

  //update 메소드
  public void updateTimestamp() {
    this.updatedAt = Instant.now();
  }

}
