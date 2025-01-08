package com.sprint.misson.discordeit.entity;

import java.util.UUID;

public class Channel {
    //객체 식별 id
    private UUID id;
    //채널명
    private String channelName;
    //생성 날짜 - 유닉스 타임스탬프
    private Long createdAt;
    //수정 시간
    private Long updatedAt;

    public Channel(){
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = createdAt;
    }

}
