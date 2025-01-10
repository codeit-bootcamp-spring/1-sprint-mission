package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message extends BaseEntity {
    private String content; // 메세지 내용
    private UUID senderId;  // 발신자 id
    private UUID channelId; // 채널 id

    public Message(String content, UUID senderId, UUID channelId) {
        super();
        this.content = content;
        this.senderId = senderId;
        this.channelId = channelId;
    }

    public String getContent() {
        return content;
    }

    public void updateContent(String content) {
        this.content = content;
        setUpdateAT(System.currentTimeMillis());
    }

    public UUID getSenderId() {
        return senderId;
    }

    public UUID getChannelId() {
        return channelId;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + getId() +
                ", content='" + content + '\'' +
                ", senderId=" + senderId +
                ", channelId=" + channelId +
                ", createdAt=" + getCreatedAt() +
                ", updatedAt=" + getUpdateAT() +
                '}';
    }

}

