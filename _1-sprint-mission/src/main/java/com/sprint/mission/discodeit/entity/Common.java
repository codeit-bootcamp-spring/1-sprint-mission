package com.sprint.mission.discodeit.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import lombok.Getter;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;
@MappedSuperclass // 이 클래스를 직접 Entity로 사용하지 않음
@Getter
public abstract class Common implements Serializable {
    private static final long serialVersionUID = 1L; //직렬화 버전
    //필드
    @Transient //JPA가 관리하는 필드가 아님
    private final UUID id = UUID.randomUUID();
    private final Instant createdAt = Instant.now();
    private Instant updatedAt = createdAt;

    //update 메소드
    public void updateTimestamp() {
        this.updatedAt = Instant.now();
    }
}
