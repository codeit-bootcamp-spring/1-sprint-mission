package com.sprint.mission.entity.addOn;

import com.sprint.mission.entity.main.BaseUpdatableEntity;
import com.sprint.mission.entity.main.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import static jakarta.persistence.FetchType.*;

@Entity
@EqualsAndHashCode(of = {"lastActiveAt"}, callSuper = true)
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString(of = "lastActiveAt")
@Getter @Builder
@Schema(description = "유저 상태")
@Table(name = "user_statuses")
public class UserStatus extends BaseUpdatableEntity {

    @OneToOne(mappedBy = "status", cascade = CascadeType.ALL)
    private User user;

    private Instant lastActiveAt;

    public UserStatus(User user) {
        this.user = user;
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
