package com.sprint.mission.discodeit.entity.relationship;

import java.util.UUID;

public class ChannelUser {
  private UUID userId;
  private UUID channelId;
  private Long entryTime;
  private boolean admin;

  public ChannelUser(UUID userId, UUID channelId, boolean admin){
    this.userId = userId;
    this.channelId = channelId;
    this.admin = admin;
    this.entryTime = System.currentTimeMillis();
  }

  public boolean isAdmin() {
    return admin;
  }

  public Long getEntryTime() {
    return entryTime;
  }

  public UUID getUserId() {
    return userId;
  }

  public UUID getChannelId() {
    return channelId;
  }
}
