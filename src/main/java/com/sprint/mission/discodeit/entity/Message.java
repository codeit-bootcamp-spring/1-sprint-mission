package com.sprint.mission.discodeit.entity;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class Message {                  // 메시지 (게시물)
    // 공통 필드
    private final UUID id;              // pk
    private final Long createdAt;       // 생성 시간
    private Long updatedAt;             // 수정 시간

    private final UUID channelId;       // 메시지가 속해있는 채널
    private final UUID writer;          // 작성자 id
    private String message;             // 메시지 내용

    // 생성자
    public Message(UUID channelId, String message, UUID writer){
        id = UUID.randomUUID();
        createdAt = System.currentTimeMillis();

        this.channelId = channelId;
        this.message = message;
        this.writer = writer;
    }


    // Getter 함수
    public UUID getId() {
        return id;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public UUID getChannelId() {
        return channelId;
    }

    public UUID getWriter() {
        return writer;
    }

    public String getMessage() {
        return message;
    }


    // update 함수
    public void updateMessage(String message) {
        this.message = message;
        updateUpdateAt();
    }

    public void updateUpdateAt(){
        this.updatedAt = System.currentTimeMillis();
    }
}