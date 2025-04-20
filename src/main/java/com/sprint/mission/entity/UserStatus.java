package com.sprint.mission.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.time.Duration;
import java.time.Instant;

import static jakarta.persistence.FetchType.*;

@Entity
@EqualsAndHashCode(of = {"user"}, callSuper = true)
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString(of = "lastActiveAt")
@Getter
@Builder
@Schema(description = "유저 상태")
@Table(name = "user_statuses")
public class UserStatus extends BaseUpdatableEntity {

  @OneToOne(fetch = LAZY, optional = false)
  @JoinColumn(name = "user_id", nullable = false, unique = true)
  private User user;

  @Column(columnDefinition = "timestamp with the zone", nullable = false)
  private Instant lastActiveAt;

  public UserStatus(User user) {
    this.user = user;
    this.lastActiveAt = Instant.now();
  }

  public void update() {
    this.lastActiveAt = Instant.now();
  }

  public boolean isOnline() {
    if (lastActiveAt == null) {
      return false;
    }

    return Duration.between(lastActiveAt, Instant.now()).toMinutes() < 5;
  }
}
