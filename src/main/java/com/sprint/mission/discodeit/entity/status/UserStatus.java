package com.sprint.mission.discodeit.entity.status;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;

@Getter
public class UserStatus implements Serializable {

  private static final long serialVersionUID = 1L;
  //UserStatus의 id는 User의 식별자와 같습니다.
  private String id;
  private Instant createdAt;
  private Instant updatedAt;
  private static final int USER_ACTIVE_TIMEOUT_SECONDS = 5 * 60;

  public UserStatus(String userId) {
    this.id = userId;
    this.createdAt = Instant.now();
    this.updatedAt = createdAt;
  }

  public boolean isActive() {

    return Instant.now().minusSeconds(USER_ACTIVE_TIMEOUT_SECONDS).isBefore(updatedAt);
  }

  // 이 메소드가 왜 필요한지 생각해보자.
  public boolean isUpdated(Instant updatedAt) {
    boolean isUpdated = this.updatedAt != updatedAt;
    this.updatedAt = updatedAt;
    return isUpdated;
  }
}