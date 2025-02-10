package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class ReadStatus extends BaseEntity implements Serializable {
  private final UUID userid;
  private UUID channelId;
  private Long lastReadAt;
  
  public ReadStatus(UUID userid, UUID channelId) {
    this.userid = userid;
    this.channelId = channelId;
    this.lastReadAt = Instant.now().getEpochSecond();
  }
  
  public void updateLastReadAt() {
    this.lastReadAt = Instant.now().getEpochSecond();
  }
}
