package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import java.util.List;

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
    private List<UUID> attachmentIds;
    private Instant createdAt;

    public Message(String content, UUID senderId, UUID channelId) {
        this.id = UUID.randomUUID();
        this.content = content;
        this.senderId = senderId;
        this.channelId = channelId;
        this.createdAt = Instant.now();
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
