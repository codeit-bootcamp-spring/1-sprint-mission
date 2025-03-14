package com.sprint.mission.entity.main;


import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;


@MappedSuperclass
@EqualsAndHashCode(of = "id")
@Getter @EntityListeners(AuditingEntityListener.class)
//@Setter // 테스트 코드용 SETTER
public abstract class BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @CreatedDate
    @Column(updatable = false)
    private Instant createdAt;
}
