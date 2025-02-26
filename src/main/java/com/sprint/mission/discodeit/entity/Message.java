package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Message extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private UUID id;
    private String content;
    private UUID senderId;
    private UUID channelId;
    private List<UUID> attachmentIds = new ArrayList<>();
    private Instant createdAt;

    // ✅ 기존 생성자
    public Message(String content, UUID senderId, UUID channelId) {
        this.id = UUID.randomUUID();
        this.content = content;
        this.senderId = senderId;
        this.channelId = channelId;
        this.attachmentIds = new ArrayList<>();
        this.createdAt = Instant.now();
    }

    // ✅ 새로운 생성자 추가 (BasicMessageService에서 필요한 매개변수 포함)
    public Message(UUID id, String content, UUID senderId, UUID channelId, Instant createdAt) {
        this.id = id;
        this.content = content;
        this.senderId = senderId;
        this.channelId = channelId;
        this.attachmentIds = new ArrayList<>();
        this.createdAt = createdAt;
    }

    public void addAttachment(UUID attachmentId) {
        this.attachmentIds.add(attachmentId);
        setUpdatedAt(Instant.now());
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", senderId=" + senderId +
                ", channelId=" + channelId +
                ", attachmentIds=" + attachmentIds +
                ", createdAt=" + createdAt +
                ", updatedAt=" + getUpdatedAt() +
                '}';
    }
}
