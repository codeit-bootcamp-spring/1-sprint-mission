package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.dto.userstatus.UserStatusCreateDTO;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusUpdateDTO;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
public class UserStatus implements Serializable {

    private static final Long serialVersionUID = 1L;

    //사용자의 마지막 접속 시간 표현-> 온라인 상태 확인
    private UUID id;
    private final Instant createdAt;
    private Instant updatedAt;

    private final UUID userId;
    private Instant lastAccessedAt;

    public UserStatus(UUID userId){
        this.id=UUID.randomUUID();
        this.userId=userId;
        this.createdAt = Instant.now();
        this.lastAccessedAt=Instant.now();
    }

    public UserStatus(UserStatusCreateDTO userStatusCreateDTO) {
        this.id=UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt=this.createdAt;

        this.userId=userStatusCreateDTO.userId();
        this.lastAccessedAt=userStatusCreateDTO.lastAccessedAt();
    }

    //유저 온라인 상태를 마지막 접속 시간이 현재 시간으로부터 5분 이내임을 검증하고 반환하는 메서드.
    public Boolean isOnline(){
        if (lastAccessedAt != null) {
            Duration duration = Duration.between(lastAccessedAt, Instant.now());
            if (duration.toMinutes() <= 5){
                return true;
            }
        }
        return false;
    }

    public void update(UserStatusUpdateDTO userStatusUpdateDTO) {
        this.lastAccessedAt=userStatusUpdateDTO.time();
        this.updatedAt=Instant.now();
        isOnline();
    }
}
