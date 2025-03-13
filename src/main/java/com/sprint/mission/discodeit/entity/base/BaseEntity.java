package com.sprint.mission.discodeit.entity.base;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

// 모든 엔티티가 공통으로 가지고 있는 속성
@Getter
@Entity     // 상속받은 모든 클래스에 적용됨
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    protected UUID id;

    @CreatedDate // Entity가 생성될 때 자동으로 현재 날짜와 시간을 저장하는 어노테이션
    @Column(name = "create_at")
    protected Instant createdAt;
}
