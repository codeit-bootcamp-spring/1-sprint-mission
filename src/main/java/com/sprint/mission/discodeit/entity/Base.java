package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Base {
   private final UUID id;
   private final Long createdAt;
   private Long updatedAt;

    public Base() {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = createdAt;//처음에는 생성시간과 같기 때문에 설정
    }

    public void setUpdatedAt() {
        this.updatedAt=System.currentTimeMillis();//수정하면 그 시간대로 바뀜
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public UUID getId() {
        return id;
    }
}
