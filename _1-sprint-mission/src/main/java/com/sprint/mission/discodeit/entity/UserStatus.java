package com.sprint.mission.discodeit.entity;


import lombok.Getter;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Getter
//사용자 별 마지막으로 확인된 접속 시간을 표현하는 도메인 모델
//사용자의 온라인 상태를 확인하기 위해 활용
public class UserStatus extends Common {
    private static final long serialVersionUID = 1L;

    private UUID userId; // 사용자 별로 관리하는 데이터
    private Instant lastConnectTime; //사용자의 마지막 접속 시간

    public UserStatus(UUID userId, Instant now) {
        super(UUID.randomUUID(), Instant.now());
        this.userId = userId;
        this.lastConnectTime = now;
    }

    //마지막 접속 시간 수정
    public void updateLastConnectTime(){
        this.lastConnectTime = Instant.now();
        updateTimestamp();
    }
    //마지막 접속 시간을 기준으로 현재 로그인한 유저로 판단
    //마지막 접속 시간이 현재 시간으로부터 5분 이내이면 현재 접속 중인 유저
    public Boolean isOnline(){
        Instant fiveMinutesAgo = Instant.now().minus(Duration.ofMinutes(5));
        return lastConnectTime.isAfter(fiveMinutesAgo);
    }
}
