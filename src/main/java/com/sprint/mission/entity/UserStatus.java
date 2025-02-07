package com.sprint.mission.entity;

import lombok.Getter;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class UserStatus {

    //마지막으로 확인된 접속 시간을 표현
    // 사용자의 온라인 상태를 확인하기 위해 활용

    // 마지막 접속 시간을 기준으로 현재 로그인한 유저로 판단할 수 있는 메소드를 정의
    // 마지막 접속 시간이 현재 시간으로부터 5분 이내이면 현재 접속 중인 유저로 간주
    private final UUID userId;
    private Instant lastJoinTime;

    public UserStatus(UUID userId){
        this.userId = userId;
        lastJoinTime = Instant.now();
    }

    public void join(){
        lastJoinTime = Instant.now();
    }

    public boolean isOnline(){
        if (lastJoinTime == null) return false;
        else return Duration.between(lastJoinTime, Instant.now()).toMinutes() < 5;
    }
}
