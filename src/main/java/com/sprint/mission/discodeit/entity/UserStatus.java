package com.sprint.mission.discodeit.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class UserStatus {
    @Id @GeneratedValue
    private UUID id;
    private UUID userId;
    private Instant lastSeen;
    private Instant createdAt;

    public UserStatus(UUID id, Instant lastSeen) {
        this.id = id;
        this.lastSeen = lastSeen;
        this.createdAt = Instant.now();
    }

    public Instant getLastSeen() {
        return lastSeen;
    }

}