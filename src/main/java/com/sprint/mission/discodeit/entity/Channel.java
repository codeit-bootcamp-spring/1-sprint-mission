package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.util.UUID;

public class Channel implements Serializable {
  private static final long serialVersionUID = 1L;

  private UUID id;
  private Long createdAt;
  private Long updatedAt;
  private String name;
  private String topic;

  public Channel(String name, String topic) {
    this.id = UUID.randomUUID();
    this.createdAt = System.currentTimeMillis();
    this.updatedAt = this.createdAt;
    this.name = name;
    this.topic = topic;
  }

  public UUID getId() {
    return id;
  }

  public Long getCreatedAt() {
    return createdAt;
  }

  public Long getUpdatedAt() {
    return updatedAt;
  }

  public String getName() {
    return name;
  }

  public String getTopic() {
    return topic;
  }

  public void update(String name, String topic) {
    this.name = name;
    this.topic = topic;
    this.updatedAt = System.currentTimeMillis();
  } // Repository 계층으로 넣을 예정

  @Override
  public String toString() {
    return "Channel{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", topic='" + topic + '\'' +
        '}';
  }
}