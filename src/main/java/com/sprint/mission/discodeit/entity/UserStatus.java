package com.sprint.mission.discodeit.entity;

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
    private final UUID userId;
    private final Instant createdAt;
    private Instant updatedAt;
    private Instant lastAccessedAt;
    private Boolean Online; //없어도 될까?

    public UserStatus(UUID userId){
        this.id=UUID.randomUUID();
        this.userId=userId;
        this.createdAt = Instant.now();
    }

    //마지막 접속 시간이 현재 시간으로부터 5분 이내임을 판별하는 메서드, 온라인으로 간주
    public Boolean isOnline(){
        if (lastAccessedAt != null) {
            Duration duration = Duration.between(lastAccessedAt, Instant.now());
            return duration.toMinutes() <= 5; // 마지막 접속 시간과 현재 시간 차이가 5분 이내인 경우
        }
        return false;
    }
}
