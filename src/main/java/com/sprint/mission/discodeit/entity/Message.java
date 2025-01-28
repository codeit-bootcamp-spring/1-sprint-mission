package com.sprint.mission.discodeit.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class Message implements Serializable {
  private static final long serialVersionUID = 1L;

  private final UUID id;
  private final Long createdAt;
  private Long updatedAt;
  private UUID userId;
  private UUID channelId;
  private String content;

  @JsonCreator
  public Message(
      @JsonProperty("id") UUID id,
      @JsonProperty("createdAt") Long createdAt
  ) {
    this.id = id != null ? id : UUID.randomUUID();
    this.createdAt = createdAt != null ? createdAt : System.currentTimeMillis();
  }

  public Message(UUID userId, String content, UUID channelId) {
    this.id = UUID.randomUUID();
    this.createdAt = System.currentTimeMillis();
    this.userId = userId;
    this.content = content;
    this.channelId = channelId;
    this.updatedAt = createdAt;
  }

  public Message(Message message) {
    this.id = message.id;
    this.createdAt = message.createdAt;
    this.userId = message.userId;
    this.content = message.content;
    this.channelId = message.channelId;
    this.updatedAt = message.updatedAt;
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

  public UUID getUserId() {
    return userId;
  }

  public UUID getChannelId() {
    return channelId;
  }

  public String getContent() {
    return content;
  }



  public void updateMessage(String message) {
    this.content = message;
    this.updatedAt = System.currentTimeMillis();
  }

  public void setUpdatedAt(Long updatedAt) {
    this.updatedAt = updatedAt;
  }

  public void setUserId(UUID userId) {
    this.userId = userId;
  }

  public void setChannelId(UUID channelId) {
    this.channelId = channelId;
  }

  public void setContent(String content) {
    this.content = content;
  }


  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (obj instanceof Message) {
      Message message = (Message) obj;
      return Objects.equals(this.id, message.id);
    }

    if (obj instanceof String) {
      return Objects.equals(this.id.toString(), obj);
    }

    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}