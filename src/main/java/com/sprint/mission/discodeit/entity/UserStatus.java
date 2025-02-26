package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.util.UserStatusType;
import com.sprint.mission.discodeit.util.UuidGenerator;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

@Getter
@Setter
public class UserStatus implements Serializable {
  private static final long serialVersionUID = 1L;

  private String id;
  private String userId;
  private Instant createdAt;
  private Instant lastOnlineAt;
  private UserStatusType userStatus;
  private Instant updatedAt;

  public UserStatus(String userId, Instant lastOnlineAt) {
    this.id = UuidGenerator.generateid();
    this.userId = userId;
    this.createdAt = Instant.now();
    this.updatedAt = Instant.now();
    this.lastOnlineAt = lastOnlineAt;
    this.userStatus = UserStatusType.ONLINE;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    UserStatus status = (UserStatus) o;
    return Objects.equals(id, status.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  public void updateLastOnline(Instant now) {
    updatedAt = now;
    lastOnlineAt = now;
  }


}
