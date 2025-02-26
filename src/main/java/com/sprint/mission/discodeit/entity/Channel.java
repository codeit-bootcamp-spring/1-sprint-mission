package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
public class Channel implements Serializable {

  private static final long serialVersionUID = 1L;
  private UUID id;
  private Instant createdAt;
  private Instant updatedAt;

  private ChannelType type;
  private String name;
  private String description;

  public Channel(ChannelType type, String name, String description) {
    this.id = UUID.randomUUID();
    this.createdAt = Instant.now();
    //
    this.type = type;
    this.name = name;
    this.description = description;
  }

  public void update(String channelName, String description) {
    boolean flag = false;
    if (channelName != null && !channelName.equals(this.name)) {
      this.name = name;
      flag = true;
    }
    if (description != null && !description.equals(this.description)) {
      this.description = description;
      flag = true;
    }
    if (flag) {
      this.updatedAt = Instant.now();
    }
  }
}
