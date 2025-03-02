package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class Channel implements Serializable {

  private static final long serialVersionUID = 1L;
  private final UUID id;
  private String name;
  private String description;
  private ChannelType type;
  private final Instant createdAt;
  private Instant updatedAt;

  public Channel(ChannelType type, String name, String description) {
    this.id = UUID.randomUUID();
    this.name = name;
    this.type = type;
    this.description = description;
    this.createdAt = Instant.now();
    this.updatedAt = Instant.now();
  }

  public void updateChannelName(String newName) {
    this.name = newName;
    this.updatedAt = Instant.now();
  }

  public void updateDescription(String newDescription) {
    this.description = newDescription;
    this.updatedAt = Instant.now();
  }

}