package com.sprint.mission.discodeit.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserStatus implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;

    private UUID userId;
    private Instant lastActiveAt;

    public UserStatus() {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
    }

    public UserStatus(UUID userId, Instant lastActiveAt) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        //
        this.userId = userId;
        this.lastActiveAt = lastActiveAt;
    }

    public void update(Instant lastActiveAt) {
        boolean anyValueUpdated = false;
        if (lastActiveAt != null && !lastActiveAt.equals(this.lastActiveAt)) {
            this.lastActiveAt = lastActiveAt;
            anyValueUpdated = true;
        }

        if (anyValueUpdated) {
            this.updatedAt = Instant.now();
        }
    }

    @JsonProperty("online")
    public Boolean isOnline() {
        Instant instantFiveMinutesAgo = Instant.now().minus(Duration.ofMinutes(5));

        return lastActiveAt.isAfter(instantFiveMinutesAgo);
    }
}