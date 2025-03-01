package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class UserStatus implements Serializable {

  private static final long serialVersionUID = 1L;
  private final long ADDITIONAL_TIME_SECONDS = 60 * 5;
  private final UUID id;
  private Instant createdAt;
  private Instant updatedAt;
  private UUID userId;
  private Instant lastActiveAt;
  private Boolean isOnline;

  public static UserStatus createUserStatus(UUID userId) {
    return new UserStatus(userId, true);
  }

  private UserStatus(UUID userId, Boolean online) {
    this.id = UUID.randomUUID();
    this.createdAt = Instant.now();
    this.updatedAt = this.createdAt;
    this.userId = userId;
    this.lastActiveAt = Instant.now();
    this.isOnline = online;
  }

  public void update(Instant lastActiveAt) {
    this.lastActiveAt = lastActiveAt;
    Instant ValidTime = this.lastActiveAt.plusSeconds(ADDITIONAL_TIME_SECONDS);
    if (ValidTime.compareTo(Instant.now()) > 0) {
      this.isOnline = true;
    } else {
      this.isOnline = false;
    }
    this.updatedAt = Instant.now();
  }

  @Override
  public String toString() {
    return "UserStatus{id:" + id
        + ",userId:" + userId
        + ",online:" + isOnline
        + ",lastActiveAt:" + lastActiveAt
        + ",createdAt:" + createdAt
        + ",updateAt:" + updatedAt
        + "}";
  }

}
