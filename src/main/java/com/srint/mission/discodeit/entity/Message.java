package com.srint.mission.discodeit.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

public class Message implements Serializable {

    private final UUID id;
    private final long createdAt;
    private long updatedAt;

    private String content;
    private UUID authorId;
    private UUID channelId;

    public Message(String content, UUID authorId, UUID channelId) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now().getEpochSecond();

        this.content = content;
        this.authorId = authorId;
        this.channelId = channelId;
    }

    public UUID getId() {
        return id;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public String getContent() {
        return content;
    }

    public UUID getAuthorId() {
        return authorId;
    }

    public UUID getChannelId() {
        return channelId;
    }

    public void setUpdatedAt() {
        this.updatedAt = Instant.now().getEpochSecond();
    }

    public void setContent(String content) {
        if(content ==null && content.equals(this.content)){
            throw new IllegalArgumentException("입력한 값이 null 혹은 중복입니다.");
        }
        this.content = content;
        setUpdatedAt();
    }

    public static boolean validation(String content){
        if (content == null || content.isEmpty()) {
            return false;
        }
        return true;
    }


    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", content='" + content + '\'' +
                ", authorId=" + authorId +
                ", channelId=" + channelId +
                '}';
    }
}