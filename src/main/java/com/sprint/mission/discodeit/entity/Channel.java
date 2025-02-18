package com.sprint.mission.discodeit.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Channel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.PRIVATE)
    private  UUID id;

    @Setter(AccessLevel.PRIVATE)
    private Instant createdAt;

    private Instant updatedAt;
    private String name;

    @Setter(AccessLevel.PRIVATE)
    private ChannelType type;

    public Channel(String name, ChannelType type) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.name = name;
        this.type = type;
    }//for public type
    public Channel(ChannelType type) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.name = null;
        this.type = type;
    }//for private type

    public Channel(UUID id, Instant createdAt, ChannelType type) {
        this.id = id;
        this.createdAt = createdAt;
        this.type = type;
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
