package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

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
                ", userId=" + userId +
                ", channelId=" + channelId +
                '}';
    }
}