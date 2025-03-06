package com.sprint.mission.entity.addOn;

import com.sprint.mission.config.BaseTimeEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Getter
@Schema(description = "유저 상태")
public class UserStatus extends BaseTimeEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private UUID id;
//    private Instant createdAt;
//    private Instant updatedAt;
    private UUID userId;
    private Instant lastActiveAt;

    public UserStatus(UUID userId) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.lastActiveAt = Instant.now();
    }

    public void update() {
        this.lastActiveAt = Instant.now();
    }

    public boolean isOnline(){
        if (lastActiveAt == null) return false;
        else return Duration.between(lastActiveAt, Instant.now()).toMinutes() < 5;
    }
}
