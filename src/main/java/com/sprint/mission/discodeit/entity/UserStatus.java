package com.sprint.mission.discodeit.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.Instant;

@Entity
@Table(name = "user_statuses")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserStatus extends BaseUpdatableEntity {

    @Column(name = "last_active_at", nullable = false, columnDefinition = "timestampz default now()")
    private Instant lastActiveAt; // 접속 시간

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_user"), nullable = false)
    private User user;

    public UserStatus(Instant lastActiveAt, User user) {
        this.lastActiveAt = lastActiveAt;
        this.user = user;
    }

    public void updateLastActiveAt(Instant lastActiveAt) {
        this.lastActiveAt = lastActiveAt;
    }

    public void updateLastActiveAt() {
        this.lastActiveAt = Instant.now();
    }

    public void updateUser(User user) {
        this.user = user;
    }

    public boolean isOnline() {
        return Duration.between(Instant.now(), lastActiveAt).toMinutes() <= 5L;
    }

    @Override
    public String toString() {
        return "UserStatus{" +
                "lastActiveAt=" + lastActiveAt +
                '}';
    }
}
