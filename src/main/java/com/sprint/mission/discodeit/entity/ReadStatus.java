package com.sprint.mission.discodeit.entity;


import com.sprint.mission.discodeit.util.UuidGenerator;
import lombok.Getter;

import java.time.Instant;

@Getter
public class ReadStatus {
  private final String UUID;
  private final String channelId;
  private final String userId;
  private final Instant createdAt;
  private Instant lastReadAt;
  private Instant updatedAt;

  public ReadStatus(String channelId, String userId){
    this.UUID = UuidGenerator.generateUUID();
    this.channelId = channelId;
    this.userId = userId;
    this.createdAt = Instant.now();
    this.updatedAt = Instant.now();
    this.lastReadAt = Instant.now();
  }

  public void updateLastReadAt(){
    updatedAt = Instant.now();
    lastReadAt = Instant.now();
  }
}
