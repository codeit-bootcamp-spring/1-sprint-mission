package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.util.UserStatusType;
import com.sprint.mission.discodeit.util.UuidGenerator;
import lombok.Getter;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

@Getter
public class UserStatus implements Serializable {
  private static final long serialVersionUID = 1L;

  private final String UUID;
  private final String userId;
  private final Instant createdAt;
  private Instant lastOnlineAt;
  private UserStatusType userStatus;
  private Instant updatedAt;

  public UserStatus(String userId, Instant lastOnlineAt) {
    this.UUID = UuidGenerator.generateUUID();
    this.userId = userId;
    this.createdAt = Instant.now();
    this.updatedAt = Instant.now();
    this.lastOnlineAt = lastOnlineAt;
    updateStatus();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    UserStatus status = (UserStatus) o;
    return Objects.equals(UUID, status.UUID);
  }

  @Override
  public int hashCode() {
    return Objects.hash(UUID);
  }

  public void updateLastOnline() {
    Instant now = Instant.now();
    updatedAt = now;
    lastOnlineAt = now;
    updateStatus();
  }

  private void updateStatus() {
    long minutesSinceLastSeen = Duration.between(lastOnlineAt, Instant.now()).toMinutes();
    if (minutesSinceLastSeen <= 5) {
      userStatus = UserStatusType.ONLINE;
    } else if (minutesSinceLastSeen <= 30) {
      userStatus = UserStatusType.IDLE;
    } else {
      userStatus = UserStatusType.OFFLINE;
    }
  }
}
