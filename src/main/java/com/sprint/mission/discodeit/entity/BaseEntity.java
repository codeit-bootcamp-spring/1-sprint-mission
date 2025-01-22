package com.sprint.mission.discodeit.entity;
import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

public class BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    // 공통 필드 작성
    private transient UUID id;
    private String idString;
    private final Long createdAt;
    private Long updatedAt;

    public BaseEntity(){
        // UUID 생성시키기
        this.id = UUID.randomUUID();
        this.idString = id.toString();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
    }

    public UUID getId(){
        if( id == null && idString != null){
            id = UUID.fromString(idString);
        }
        return id;}
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

