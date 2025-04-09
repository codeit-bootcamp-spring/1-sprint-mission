package com.sprint.mission.discodeit.entity.base;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class) // TODO for @CreatedDate
// TODO BaseEntity는 추상클래스에 상속 자체만이 목적이므로 @MappedSuperclass를 써줘야한다. @Entity로 설정하면 스프링이 엔티티로 인식해서 테이블로 설정하려고 한다.
// TODO 스프링 부트 메인클래스(application)에서 @EnableJpaAuditing으로 JPA Auditing 활성화
public abstract class BaseEntity {

  @GeneratedValue(generator = "uuid2")
  @GenericGenerator(name = "uuid2", strategy = "uuid2")
  @Column(columnDefinition = "BINARY(16)", updatable = false, nullable = false)
  @Id
  // Hibernate와 Spring Data JPA는 기본적으로 Jakarta Persistence API(JPA 3.0) 를 따르기 때문에, @Id도 Jakarta의 것을 써야 함.
  protected UUID id;

  @CreatedDate
  @Column(name = "created_at", nullable = false)
  protected Instant createdAt; // TODO 기본생성자는 그냥 null로 초기화하기 때문에 기본값 직접 넣어줘야함
}
