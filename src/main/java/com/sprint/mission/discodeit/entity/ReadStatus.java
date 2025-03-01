package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class ReadStatus implements Serializable {

  private static final long serialVersionUID = 1L;
  private final UUID id;
  private Instant createdAt;
  private Instant updatedAt;
  private UUID userId;
  private UUID channelId;
  private Instant lastReadAt;

  public static ReadStatus createReadStatus(UUID userId, UUID channelId, Instant lastReadAt) {
    return new ReadStatus(userId, channelId, lastReadAt);
  }

  private ReadStatus(UUID userId, UUID channelId, Instant lastReadAt) {
    this.id = UUID.randomUUID();
    this.createdAt = Instant.now();
    this.updatedAt = createdAt;
    this.userId = userId;
    this.channelId = channelId;
    this.lastReadAt = lastReadAt;
  }

  public void updateUpdateAt(Instant lastReadAt) {
    this.lastReadAt = lastReadAt;
    this.updatedAt = Instant.now();
  }

  @Override
  public String toString() {
    return "ReadStatus{id:" + id
        + ",userId:" + userId
        + ",channelId:" + channelId
        + ",createdAt:" + createdAt
        + ",updateAt:" + updatedAt
        + "}";
  }

}
