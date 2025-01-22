package com.sprint.mission.discodeit.entity;
import java.io.Serial;
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

    public UUID getId(){return id;}
    // id, createdAt 상수로 변경
    // public void setId(UUID id){ this.id = id;}
    public Long getCreatedAt(){
        return createdAt;
    }
    // public void setCreatedAt(Long createdAt){this.createdAt = createdAt;}
    public Long getUpdatedAt(){
        return updatedAt;
    }
    public void setUpdateAt(Long updatedAt){
        this.updatedAt = updatedAt;
    }
}

