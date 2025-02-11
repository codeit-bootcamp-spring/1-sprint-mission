package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.dto.userstatus.UserStatusCreateDTO;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusUpdateDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
public class UserStatus {
    //사용자의 마지막 접속 시간 표현-> 온라인 상태 확인
    private UUID id;
    private final Instant createdAt;
    private Instant updatedAt;

    private final UUID userId;
    private Instant lastAccessedAt;
    private Boolean onlineStatus;

    public UserStatus(UUID userId){
        this.id=UUID.randomUUID();
        this.userId=userId;
        this.createdAt = Instant.now();
        this.lastAccessedAt=Instant.now();
        this.onlineStatus=isOnline();
    }

    public UserStatus(UserStatusCreateDTO userStatusCreateDTO) {
        this.id=UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt=this.createdAt;

        this.userId=userStatusCreateDTO.userId();
        this.lastAccessedAt=userStatusCreateDTO.lastAccessedAt();
        this.onlineStatus=isOnline();
    }

    //유저 온라인 상태 반환. 마지막 접속 시간이 현재 시간으로부터 5분 이내임을 판별하는 메서드.
    public Boolean isOnline(){
        if (lastAccessedAt != null) {
            Duration duration = Duration.between(lastAccessedAt, Instant.now());
            if (duration.toMinutes() <= 5){
                onlineStatus=true;
            }
            else onlineStatus=false;
        }
        return onlineStatus;
    }

    public void update(UserStatusUpdateDTO userStatusUpdateDTO) {



    }
}
