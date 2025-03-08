package com.sprint.mission.entity.addOn;

import com.sprint.mission.entity.main.BaseUpdatableEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@ToString @Getter
@Schema(description = "유저 상태")
public class UserStatus extends BaseUpdatableEntity {

    private UUID userId;
    private Instant lastActiveAt;

    public UserStatus(UUID userId) {
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
