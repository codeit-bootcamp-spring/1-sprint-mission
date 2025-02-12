package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class UserStatus implements Serializable, Entity{

    private UUID id;
    private Instant createdAt;
    private Instant updatedAt;


    // 생성자
    public UserStatus() {
        id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    // 업데이트시간 변경
    public void setUpdatedAt() {
        updatedAt = Instant.now();
    }

    // 마지막으로 접속 확인된 시간과 현재 시간을 비교하여 그 차이가 5분 미만이면 true(온라인) 반환, 아니라면 false(오프라인)반환
    public boolean isUserOnline(){
        long now = Instant.now().getEpochSecond();
        long lastUpdatedAt = updatedAt.getEpochSecond();

        if (now < lastUpdatedAt+300){
            return true;
        } else {
            return false;
        }
    }


}
