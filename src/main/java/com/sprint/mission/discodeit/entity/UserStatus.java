package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.execption.ErrorCode;
import com.sprint.mission.discodeit.common.util.TimeUtil;
import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_statuses")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserStatus extends BaseUpdatableEntity {

  @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST,
      CascadeType.REMOVE}, orphanRemoval = true)
  @JoinColumn(name = "user_id", nullable = false, unique = true)
  private User user;

  @Column(name = "last_active_at", nullable = false)
  private Instant loginAt;

  private static final long LOGIN_EXPIRATION_MINUTES = 5;

  private UserStatus(User user) {
    super();
    this.user = user;
    this.loginAt = TimeUtil.getCurrentTime();
  }

  public static UserStatus of(User user) {
    if (user == null) {
      throw new IllegalArgumentException(ErrorCode.USER_NOT_NULL.getMessage());
    }
    return new UserStatus(user);
  }

  public void updateLoginAt(Instant newLastActiveAt) {
    this.loginAt = newLastActiveAt;
    this.updateUpdatedAt();
  }

  public boolean isRecentLogin() {
    if (this.getUpdatedAt().equals(this.getCreatedAt())) {
      return false;
    }
    return ChronoUnit.MINUTES.between(Instant.now(), this.loginAt) < LOGIN_EXPIRATION_MINUTES;
  }

  public void updateUserStatusInfo(Instant newLastActiveAt) {
    this.updateLoginAt(newLastActiveAt);
  }
}
