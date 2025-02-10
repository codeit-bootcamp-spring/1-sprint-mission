package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
public class BaseEntity {
    //추후 엔티티 공통 분모 extends 처리 예정

    private UUID id ;
    private Instant createdAt;
    private Instant updatedAt;

    BaseEntity(){
        this.id = UUID.randomUUID();
        this.createdAt =  Instant.now();
        this.updatedAt=this.createdAt;
    }

    void update(){
        updatedAt =  Instant.now();
    }

}
