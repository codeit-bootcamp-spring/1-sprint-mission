package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.Type.UserStatusType;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class UserStatus implements Serializable, Entity{

    private UUID userId;
    private Instant createdAt;
    private Instant updatedAt;
    private UserStatusType statusType;


    // 생성자
    public UserStatus(UUID userId) {
        this.userId = userId;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.statusType = UserStatusType.OFFLINE;
    }

    // 업데이트시간 변경
    public void setUpdatedAt() {
        updatedAt = Instant.now();
    }

    // 마지막으로 접속 확인된 시간과 현재 시간을 비교하여 그 차이가 5분 미만이면 온라인, 이상이면 오프라인으로 설정
    public void updateStatus() {
        long now = Instant.now().getEpochSecond();
        long lastUpdatedAt = updatedAt.getEpochSecond();
        if (now < lastUpdatedAt+300){
            this.statusType=UserStatusType.ONLINE;
        }else{
            this.statusType=UserStatusType.OFFLINE;
        }
    }

    // 유저 온라인 여부 리턴
    public boolean isUserOnline(){
        updateStatus();
        return this.statusType.equals(UserStatusType.ONLINE) ? true : false;
    }

}
