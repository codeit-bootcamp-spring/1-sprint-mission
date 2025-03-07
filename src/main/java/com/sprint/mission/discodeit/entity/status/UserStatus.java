package com.sprint.mission.discodeit.entity.status;

import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;

@Getter
public class UserStatus extends BaseUpdatableEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  @OneToOne
  @JoinColumn(name = "user_id")
  private UserDto user;
  private Instant lastActiveAt;

  public UserStatus(UserDto user) {
    this.user = user;
  }

  private static final int USER_ACTIVE_TIMEOUT_SECONDS = 5 * 60;

  public boolean isActive() {
    return Instant.now().minusSeconds(USER_ACTIVE_TIMEOUT_SECONDS).isBefore(this.getUpdatedAt());
  }

  // 이 메소드가 왜 필요한지 생각해보자.
  public boolean isUpdated(Instant updatedAt) {
    return this.getUpdatedAt() != updatedAt;
  }
}