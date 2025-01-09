package com.sprint.mission.discodeit.entity;
import java.util.UUID;

public class BaseEntity {
    // 공통 필드 작성
    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;

    public BaseEntity(){
        // UUID 생성시키기
        this.id = UUID.randomUUID();;
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
    }
    // id, createdAt은 setter 생략
    protected UUID getId(){
        return id;
    }
    public Long getCreatedAt(){
        return createdAt;
    }
    public Long getUpdatedAt(){
        return updatedAt;
    }
    public void setUpdateAt(Long updatedAt){
        this.updatedAt = updatedAt;
    }
}