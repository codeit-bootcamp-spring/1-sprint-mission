package com.sprint.mission.discodeit.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Getter
@Entity
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;
    private UUID senderId;
    private UUID channelId;
    private String content;

    public Message(UUID sender, UUID channelId, String content) {
        this.id = UUID.randomUUID();
        this.senderId = sender;
        this.channelId = channelId;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.content = content;
    }

    public Message() {
        this.id = null;
        this.content = "삭제된 메시지 입니다";
        this.createdAt = Instant.ofEpochSecond(0L);
    }

    public Instant setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
        return updatedAt;
    }

    public void update(String content) {
        this.content = content;
        updatedAt = setUpdatedAt(Instant.now());
    }

    @Override
    public String toString() {
        return "Message{" + "id=" + id + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + ", senderId=" + senderId + ", channelId=" + channelId + ", content='" + content + '\'' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // 같은 객체라면 true
        if (o == null || getClass() != o.getClass()) return false; // null이거나 클래스가 다르면 false
        Message message = (Message) o;
        return Objects.equals(content, message.content) && Objects.equals(channelId, message.channelId) && Objects.equals(senderId, message.senderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, content, channelId, senderId, createdAt, updatedAt); // 비교 대상 필드 기반 hashCode 생성
    }
}
