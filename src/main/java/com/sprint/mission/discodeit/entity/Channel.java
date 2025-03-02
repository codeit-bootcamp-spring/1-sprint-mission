package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class Channel implements Serializable {

  private static final long serialVersionUID = 1L;
  private final UUID id;
  private String name;  // channelName에서 name으로 변경
  private String description;
  private ChannelType type;
  private final Instant createdAt;
  private Instant updatedAt;

  public Channel(ChannelType type, String name, String description) {
    this.id = UUID.randomUUID();
    this.name = name;  // channelName에서 name으로 변경
    this.type = type;
    this.description = description;
    this.createdAt = Instant.now();
    this.updatedAt = Instant.now();
  }

  public void updateChannelName(String newName) {
    this.name = newName;  // channelName에서 name으로 변경
    this.updatedAt = Instant.now();
  }

  public void updateDescription(String newDescription) {
    this.description = newDescription;
    this.updatedAt = Instant.now();
  }

}