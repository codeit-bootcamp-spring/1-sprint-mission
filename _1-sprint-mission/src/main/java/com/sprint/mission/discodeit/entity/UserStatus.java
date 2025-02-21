package com.sprint.mission.discodeit.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Getter
@NoArgsConstructor
@Entity
//사용자 별 마지막으로 확인된 접속 시간을 표현하는 도메인 모델
//사용자의 온라인 상태를 확인하기 위해 활용
public class UserStatus extends Common {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private UUID id;

    @OneToOne
    @JoinColumn(name = "user_id",nullable=false,unique = true)
    private User user;//해당 상태를 가진 사용자

    private Instant lastConnectTime; //사용자의 마지막 접속 시간
    private boolean onlineStatus; // 온라인 상태

    public UserStatus(User user, Instant lastConnectTime, boolean onlineStatus) {
        this.user = user;
        this.lastConnectTime = lastConnectTime;
        this.onlineStatus = onlineStatus;
    }

    //마지막 접속 시간 수정
    public void updateLastConnectTime(){
        this.lastConnectTime = Instant.now();
        updateTimestamp();
    }
    //마지막 접속 시간을 기준으로 현재 로그인한 유저로 판단
    //마지막 접속 시간이 현재 시간으로부터 5분 이내이면 현재 접속 중인 유저
    public boolean loginUserCheck(){
        Instant plusFiveMinutes = Instant.now().plusSeconds(300);
        if(lastConnectTime.isBefore(plusFiveMinutes)){
            onlineStatus = true;
        }else{
            onlineStatus = false;
        }
        return onlineStatus;
    }
}
