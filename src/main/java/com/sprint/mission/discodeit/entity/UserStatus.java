package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Getter
@Table(name = "user_statuses")
public class UserStatus extends BaseUpdatableEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private final User user;

    @Column(name = "last_active_at")
    private Instant lastActiveAt; // 최종 접속 시간

    public UserStatus(User user) {
        this.user = user;
        this.lastActiveAt = Instant.now();
    }

    public void updateLastAccessTime() {
        this.lastActiveAt = Instant.now();
    }

    // 현재 유저가 접속해있는지 판별하는 메서드
    // 현재 시간 기준으로 마지막 접속으로부터 5분 이내이면 접속 중으로 판단
    public boolean checkAccess() {
        Instant now = Instant.now();

        Duration between = Duration.between(lastActiveAt, now);

        return between.getSeconds() <= 300;     // 접속 중이면 true, 아니면 false 반환
    }
}
