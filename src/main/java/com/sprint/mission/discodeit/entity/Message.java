package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.util.UUID;

public class Message extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L; // 직렬화 버전 ID

    private String content;
    private UUID senderId;
    private UUID channelId;

    public Message(String content, UUID senderId, UUID channelId) {
        super();
        this.content = content;
        this.senderId = senderId;
        this.channelId = channelId;
    }

    public String getContent() { return content; }
    public void updateContent(String content) {
        this.content = content;
        setUpdateAT(System.currentTimeMillis());
    }

    public UUID getSenderId() { return senderId; }
    public UUID getChannelId() { return channelId; }

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
