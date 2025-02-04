package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Getter
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;
    private UUID senderId;
    private String content;

    public Message(UUID sender, String content) {
        this.id = UUID.randomUUID();
        this.senderId = sender;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.content = content;
    }
    public Message() {
        this.id = null;
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
        return "Message{" + "id=" + id + ", senderId=" + senderId + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + ", content='" + content + '\'' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // 같은 객체라면 true
        if (o == null || getClass() != o.getClass()) return false; // null이거나 클래스가 다르면 false
        Message message = (Message) o;
        return  Objects.equals(content, message.content); // 모든 필드를 비교

    }
    @Override
    public int hashCode() {
        return Objects.hash(id, content, createdAt, updatedAt); // 비교 대상 필드 기반 hashCode 생성
    }
}
