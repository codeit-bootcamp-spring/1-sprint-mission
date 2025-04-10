package com.sprint.mission.discodeit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "user_statuses")
@Getter
@Builder
@AllArgsConstructor
public class UserStatus extends BaseUpdatableEntity{
    @Column(name = "user_id")
    private UUID userid;

    @Column(name = "last_seen_at")
    private Instant lastSeenAt;

    protected UserStatus() { }

    public UserStatus(UUID userid) {
        this.userid = userid;
        this.lastSeenAt = Instant.ofEpochMilli(System.currentTimeMillis());
    }

//    public void updateLastSeen(Instant timestamp) {
//        this.lastSeenAt = timestamp;
//        onUpdate();
//    }

    public boolean isOnline() {
        return Duration.between(lastSeenAt, Instant.now()).toMinutes() < 5;
    }
}
