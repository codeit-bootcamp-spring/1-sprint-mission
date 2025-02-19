package com.sprint.mission.discodeit.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

//@Entity
@Getter
public abstract class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
//    @Id
    private UUID id;
    private Instant createdAt;
    private Instant updatedAt;

    public BaseEntity(){
        this.id = id != null ? id : UUID.randomUUID();
//        System.out.println("새로 생성된 UUID: " + this.id); // 로그 추가
        this.createdAt = Instant.ofEpochMilli(System.currentTimeMillis());
        this.updatedAt = createdAt;
    }

    public void update(){
        this.updatedAt = Instant.ofEpochMilli(System.currentTimeMillis());
    }
}
