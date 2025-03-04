package com.spirnt.mission.discodeit.enity;

import java.io.Serializable;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import lombok.Getter;

@Getter
public class UserStatus extends Common implements Serializable {

  private static final long serialVersionUID = 1L;
  private UUID userId;
  private UserStatusType userStatusType;
  private Instant lastActiveAt; // 마지막으로 확인된 접속 시간

  public UserStatus(UUID userId, UserStatusType type, Instant lastActiveAt) {
    super();
    this.userId = userId;
    this.userStatusType = type;
    this.lastActiveAt = lastActiveAt;
  }


  public boolean isOnline() {
    if (lastActiveAt == null) {
      return false; // 접속한 적이 없으면 오프라인으로 간주
    }
    Instant fiveMinutesAgo = Instant.now().minus(5, ChronoUnit.MINUTES);
    return lastActiveAt.isAfter(fiveMinutesAgo); // 5분 이내 접속이면 온라인
  }

  public void update(Instant lastActiveAt) {
    boolean anyValueUpdated = false;
    if (lastActiveAt != null && !lastActiveAt.equals(this.lastActiveAt)
        && this.lastActiveAt.isBefore(lastActiveAt)) {
      this.lastActiveAt = lastActiveAt;
      anyValueUpdated = true;
    }
    if (anyValueUpdated) {
      updateClass(Instant.now());
    }
  }

  @Override
  public String toString() {
    return "UserStatus[User: " + this.userId +
        "(" + userStatusType + ")" +
        " LastSeenAt: " + lastActiveAt + "]";
  }

}
