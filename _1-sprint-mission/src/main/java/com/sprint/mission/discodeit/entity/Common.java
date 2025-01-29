package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

public abstract class Common implements Serializable {
    private static final long serialVersionUID = 1L; //직렬화 버전
    //필드
    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;

    //생성자
    public Common(){
        this.id=UUID.randomUUID();
        this.createdAt= Instant.now().getEpochSecond();
        this.updatedAt = this.createdAt;  // 생성 시점과 동일하게 초기화
    }
    //Getter
    public UUID getId(){
        return id;
    }
    public Long getCreatedAt(){
        return createdAt;
    }
    public Long getUpdatedAt(){
        return updatedAt;
    }

    //update 메소드
    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }
}
