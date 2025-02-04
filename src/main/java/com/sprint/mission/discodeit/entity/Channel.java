package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Getter
public class Channel implements Serializable {
    private static final long serialVersionUID = 1L;
    private final UUID id;

    private final Instant createdAt;
    private Instant updatedAt;
    private String name;

    public Channel(String name) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.name = name;
    }

    public Instant setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
        return updatedAt;
    }

    public void update(String name) {
        this.name = name;
        updatedAt = setUpdatedAt(Instant.now());
    }

    @Override
    public String toString() {
        return "Channel{" + "id=" + id + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + ", name='" + name + '\'' + '}';
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // 같은 객체라면 true
        if (o == null || getClass() != o.getClass()) return false; // null이거나 클래스가 다르면 false
        Channel channel = (Channel) o;
        return  Objects.equals(name, channel.name); // 모든 필드를 비교

    }
    @Override
    public int hashCode() {
        return Objects.hash(id, name, createdAt, updatedAt); // 비교 대상 필드 기반 hashCode 생성
    }
}
