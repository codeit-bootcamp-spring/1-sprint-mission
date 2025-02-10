package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Message implements Serializable {

    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;

    private String content;
    private UUID userId;
    private UUID channelId;
    private List<UUID> binaryContentIds;

    public Message(String content, UUID userId, UUID channelId) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();

        this.content = content;
        this.userId = userId;
        this.channelId = channelId;
        binaryContentIds = new ArrayList<>();
    }

    public UUID getId() {
        return id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public String getContent() {
        return content;
    }

    public List<UUID> getBinaryContentIds() {
        return binaryContentIds;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getChannelId() {
        return channelId;
    }

    public void setUpdatedAt() {
        this.updatedAt = Instant.now();
    }

    public void setMessage(String content) {
        if(content !=null && !content.equals(this.content)){
            this.content = content;
        }else{
            throw new IllegalArgumentException("입력한 메시지: "+content+"가 기존 값과 같습니다.");
        }
        setUpdatedAt();
    }

    public static boolean validation(String content){
        if (content == null || content.isEmpty()) {
            return false;
        }
        return true;
    }

    public void addBinaryContent(UUID binaryContentId) {
        binaryContentIds.add(binaryContentId);
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", content='" + content + '\'' +
                ", authorId=" + userId +
                ", channelId=" + channelId +
                '}';
    }
}