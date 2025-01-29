package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message {
  private UUID id;
  private Long createdAt;
  private Long updatedAt;
  private String content;
  private UUID authorId;

  public Message(String content, UUID authorId) {
    this.id = UUID.randomUUID();
    this.createdAt = System.currentTimeMillis();
    this.updatedAt = this.createdAt;
    this.content = content;
    this.authorId = authorId;
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

  public String getContent() {
    return content;
  }

  public UUID getAuthorId() {
    return authorId;
  }

  public void update(String content, UUID authorId) {
    this.content = content;
    this.authorId = authorId;
    this.updatedAt = System.currentTimeMillis();
  }

  @Override
  public String toString() {
    return "Message{" +
        "id=" + id +
        ", content='" + content + '\'' +
        ", authorId=" + authorId +
        '}';
  }
}