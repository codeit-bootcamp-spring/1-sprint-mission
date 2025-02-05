package com.sprint.mission.discodeit.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Setter;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
@Entity
@Data
public class UserStatus {
    @Id
    @GeneratedValue
    private UUID id;
    private UUID userId;
    private Instant createdAt;
    @Setter
    private Instant updatedAt;

    private Status status;

    public UserStatus(UUID userId) {
        this.userId = userId;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.status = Status.ONLINE;
    }

    public Status isOnline(){
        return Duration.between(updatedAt, Instant.now()).toMinutes() <= 5 ? Status.ONLINE : Status.OFFLINE;
    }
    public void updateStatus() {
        this.updatedAt = Instant.now();
    }
}
