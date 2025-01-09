package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message {

    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;

    // 추가적으로 필요한 필드값들은 메소드 만들면서 추가하도록 하자.
    // 지금 넣어봤자 계속 바뀔 것 같다.

    private String messageContent;

    public Message(String messageContent) {
        // 메세지를 수정, 삭제하기 위한 ID.
        id = UUID.randomUUID();
        createdAt = System.currentTimeMillis();
        this.messageContent = messageContent;
    }

    public UUID getId() {
        return id;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void updateMessage(String messageContent) {
        this.messageContent = messageContent;
    }

    public void updateUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }
}
