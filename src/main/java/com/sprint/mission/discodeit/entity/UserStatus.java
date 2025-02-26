package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
public class UserStatus implements Serializable {

  private static final long serialVersionUID = 1L;
  private UUID id;
  private Instant createdAt;
  private Instant updatedAt;

  private UUID userId;
  private Instant lastActiveAt;


  public UserStatus(UUID userId, Instant lastActiveAt) {
    this.id = UUID.randomUUID();
    this.createdAt = Instant.now();
    this.userId = userId;
    this.lastActiveAt = lastActiveAt;
  }

  public boolean isOnline() {
    return Duration.between(lastActiveAt, Instant.now()).toMinutes() < 5;
  }

  public void update(Instant lastActiveAt) {
    boolean flag = false;
    if (lastActiveAt != null && !lastActiveAt.equals(this.lastActiveAt)) {
      this.lastActiveAt = lastActiveAt;
      flag = true;
    }

    if (flag) {
      this.updatedAt = Instant.now();
    }
  }
}
