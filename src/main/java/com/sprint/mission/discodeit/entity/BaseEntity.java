package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.util.UUID;

public abstract class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private UUID id;
    private Long createdAt;
    private Long updatedAt;

    public BaseEntity(){
        this.id = id != null ? id : UUID.randomUUID();
//        System.out.println("새로 생성된 UUID: " + this.id); // 로그 추가
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = createdAt;
    }

    public UUID getId(){
        return id;
    }

    public Long getCreatedAt(){
        return createdAt;
    }

    public Long getUpdatedAt(){
        return updatedAt;
    }

    public void update(){
        this.updatedAt = System.currentTimeMillis();
    }
}
