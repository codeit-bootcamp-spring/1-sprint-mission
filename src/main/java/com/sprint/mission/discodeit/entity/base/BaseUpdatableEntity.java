package com.sprint.mission.discodeit.entity.base;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;

// 업데이트 가능한 엔티티
@Getter
@MappedSuperclass
public abstract class BaseUpdatableEntity extends BaseEntity {

    @LastModifiedDate
    // Entity가 수정될 때 자동으로 현재 날짜와 시간을 저장하는 어노테이션
    @Column(name = "updated_at")
    protected Instant updatedAt;
}
