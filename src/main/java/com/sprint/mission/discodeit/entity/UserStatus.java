package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class UserStatus extends BaseEntity implements Serializable {
  private final UUID userId;
  private Long lastOnline;
  
  public UserStatus(UUID userId) {
    this.userId = userId;
    this.lastOnline = Instant.now().getEpochSecond();
  }
  
  public void updateLastOnline() {
    super.update();
    this.lastOnline = Instant.now().getEpochSecond();
  }
  
  public boolean isOnline() {
    return Instant.now().getEpochSecond() - lastOnline < 60 * 5;
  }
}
