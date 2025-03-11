package com.sprint.mission.discodeit.entity.base;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;
import org.springframework.data.annotation.CreatedDate;

@Getter
@MappedSuperclass // 이 클래스를 상속받은 클래스에서 컬럼을 사용하도록 함
public abstract class BaseEntity {

  @Id
  @Column(name = "id")
  private UUID id;

  @CreatedDate //JPA의 어노테이션, 자동으로 created 시간을 생성
  @Column(name = "created_at")
  private Instant createdAt;

  protected BaseEntity() {
    this.id = UUID.randomUUID();
    this.createdAt = Instant.now();
  }

}
