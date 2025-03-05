package com.sprint.mission.discodeit.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "messages")
public class Message extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String content;
    // 변경: senderId -> authorId
    private UUID authorId;
    private UUID channelId;
    private List<UUID> attachmentIds = new ArrayList<>();

    // 기본 생성자: 새로운 메시지 생성 시 사용
    public Message(String content, UUID authorId, UUID channelId) {
        setId(UUID.randomUUID());
        this.content = content;
        this.authorId = authorId;
        this.channelId = channelId;
        this.attachmentIds = new ArrayList<>();
        setCreatedAt(Instant.now());
    }

    // 추가 생성자
    public Message(UUID id, String content, UUID authorId, UUID channelId, Instant createdAt) {
        setId(id);
        this.content = content;
        this.authorId = authorId;
        this.channelId = channelId;
        this.attachmentIds = new ArrayList<>();
        setCreatedAt(createdAt);
    }

    public void addAttachment(UUID attachmentId) {
        this.attachmentIds.add(attachmentId);
        setUpdatedAt(Instant.now());
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + getId() +
                ", content='" + content + '\'' +
                ", authorId=" + authorId +
                ", channelId=" + channelId +
                ", attachmentIds=" + attachmentIds +
                ", createdAt=" + getCreatedAt() +
                ", updatedAt=" + getUpdatedAt() +
                '}';
    }
}
