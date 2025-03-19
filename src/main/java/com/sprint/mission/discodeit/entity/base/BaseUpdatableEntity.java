package com.sprint.mission.discodeit.entity.base;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import java.time.Instant;
import lombok.Getter;
import org.springframework.data.annotation.LastModifiedDate;

@Getter
@MappedSuperclass // 이 클래스를 상속받은 클래스에서 컬럼을 사용하도록 함
public abstract class BaseUpdatableEntity extends BaseEntity {

  @LastModifiedDate
  @Column(name = "updated_at")
  private Instant updatedAt;

  protected BaseUpdatableEntity() {
    update();
  }

  protected void update() {
    updatedAt = Instant.now();
  }

}
