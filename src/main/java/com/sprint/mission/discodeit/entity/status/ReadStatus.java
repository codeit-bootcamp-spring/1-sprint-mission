package com.sprint.mission.discodeit.entity.status;

import com.sprint.mission.discodeit.dto.readStatus.UpdateReadStatusDto;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class ReadStatus implements Serializable {

  private static final long serialVersionUID = 1L;
  private String id;
  private Instant createdAt;
  private Instant updatedAt;
  private String channelId;
  private String userId;
  private Instant lastReadAt;

  public ReadStatus(String channelId, String userId, Instant lastReadAt) {
    this.id = UUID.randomUUID().toString();
    this.createdAt = Instant.now();
    this.updatedAt = createdAt;
    this.channelId = channelId;
    this.userId = userId;
    this.lastReadAt = createdAt;
  }

  public boolean isNewMessage() {
    return lastReadAt.isBefore(updatedAt);
  }

  public void setUpdatedAt(Instant updatedAt) {
    this.updatedAt = updatedAt;
  }

  public void setLastReadAt(Instant lastReadAt) {
    this.lastReadAt = lastReadAt;
  }
}

