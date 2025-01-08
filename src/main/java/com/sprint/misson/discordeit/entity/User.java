package com.sprint.misson.discordeit.entity;

import java.util.UUID;

public class User {
    //객체 식별용 id
    private UUID id;
    //닉네임
    private String nickname;
    //생성 날짜 - 유닉스 타임스탬프
    private Long createdAt;
    //수정 시간
    private Long updatedAt;
    //접속 상태
    private Status status;

    //사용자 설정 상태 메세지
    private String statusMessage;

    public User(){
        this.id = UUID.randomUUID();
        createdAt = System.currentTimeMillis();
        updatedAt = createdAt;
    }
}
