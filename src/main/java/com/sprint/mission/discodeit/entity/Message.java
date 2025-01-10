package com.sprint.mission.discodeit.entity;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class Message {                  // 메시지 (게시물)
    // 공통 필드
    private UUID id;                    // pk
    private long createdAt;             // 생성 시간
    private long updatedAt;             // 수정 시간

    private UUID channelId;            // 메시지가 속해있는 채널
    private String message;             // 메시지 내용
    private UUID writer;                // 작성자 id

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

    public String getMessage() {
        return message;
    }

    public UUID getWriter() {
        return writer;
    }


    // update 함수
    public void updateMessage(String message) {
        this.message = message;
        this.updatedAt = System.currentTimeMillis();
    }
}