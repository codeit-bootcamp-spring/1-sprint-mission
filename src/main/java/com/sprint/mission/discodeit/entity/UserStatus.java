package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Getter
public class UserStatus extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private UUID userId;

    private boolean isOnline;
    private Instant now;
    private Instant lastConnectTime;

    public UserStatus(UUID userId, Instant lastConnectTime){
        this.isOnline = false;
        this.now = Instant.now();
        this.userId = userId;
        this.lastConnectTime = lastConnectTime;
    }

    public void setUserId(UUID userId){
        this.userId = userId;
    }

    public void refreshNow() {
        this.now = Instant.now();
    }

    // 디스코드는 접속을 무엇으로 판단하느냐
        // 1. 로그인
        // 2. 채팅 보내기, 음성 채널 참가
        // 3. UI 조작
        // + 사용자가 설정 적용할 수 있다
    public void refreshLastConnectTime(Instant lastConnectTime){
        this.lastConnectTime = lastConnectTime;
    }

    public boolean isConnectedNow(){
        now = Instant.now();
        isOnline = Duration.between(lastConnectTime, now).toMinutes() <= 5;
        return isOnline;
    }

    @Override
    public String toString(){
        return "isOnline : " + isOnline;
    }
}
