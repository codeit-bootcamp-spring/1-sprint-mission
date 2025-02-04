package com.sprint.mission.discodeit.entity;
import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

public class BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    // 공통 필드
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
    
    public Long getCreatedAt(){
        return createdAt;
    }
    
    public Long getUpdatedAt(){
        return updatedAt;
    }
    
    // setUpdateAt -> 의도 파악이 어려워 refreshUpdateAt로 변경
    public void refreshUpdateAt(){
        this.updatedAt = System.currentTimeMillis();
    }
}

