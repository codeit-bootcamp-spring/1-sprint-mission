package com.sprint.misson.discordeit.entity;

import java.util.UUID;

public class Message {
    //객체 식별 id
    private UUID id;
    //메세지 작성자
    private User sender;
    //메세지 내용
    private String content;
    //생성 날짜 - 유닉스 타임스탬프
    private Long createdAt;
    //수정 시간
    private Long updatedAt;
    //메세지가 생성된 채널
    private Channel channel;

    //추후에 추가할 것
    //멘션, 답장(reply)

}
