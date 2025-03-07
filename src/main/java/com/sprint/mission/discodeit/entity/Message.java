package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class Message implements Serializable {

  private static final long serialVersionUID = 1L;
  private final UUID id;
  private Instant createdAt;
  private Instant updatedAt;
  private String content;
  private UUID channelId;
  private UUID authorId;

  public static Message createMessage(String content, UUID channelId, UUID userId) {
    return new Message(content, channelId, userId);
  }

  private Message(String content, UUID channelId, UUID authorId) {
    this.id = UUID.randomUUID();
    this.createdAt = Instant.now();
    this.content = content;
    this.channelId = channelId;
    this.authorId = authorId;
  }

  public void update(String newContent) {
    boolean isChanged = false;
    if (!newContent.equals(this.content)) {
      this.content = newContent;
      isChanged = true;
    }

    if (isChanged) {
      this.updatedAt = Instant.now();
    }
  }

  @Override
  public String toString() {
    return "Message{id:" + id + ",channel:" + channelId + ",user:" + authorId + ",content:" + content
        + ",createdAt:" + createdAt + ",updatedAt:" + updatedAt + "}";
  }
  
}
