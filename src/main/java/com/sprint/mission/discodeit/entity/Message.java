package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.util.List;
import lombok.Getter;

import java.io.Serializable;
import java.util.UUID;

@Getter
public class Message implements Serializable {

  private static final long serialVersionUID = 1L;
  private UUID id;
  private Instant createdAt;
  private Instant updatedAt;

  private String content;
  private final UUID channelId;
  private final UUID authorId;
  private List<UUID> attachmentIds;

  public Message(String content, UUID channelId, UUID authorId, List<UUID> attachmentIds) {
    this.id = UUID.randomUUID();
    this.createdAt = Instant.now();

    this.channelId = channelId;
    this.authorId = authorId;
    this.content = content;
    this.attachmentIds = attachmentIds;
  }

  @Override
  public String toString() {
    return "Message{" +
        "content='" + content + '\'' +
        ", channelId=" + channelId +
        ", authorId=" + authorId +
        '}';
  }

  public void update(String content) {
    boolean flag = false;
    if (content != null && !content.equals(this.content)) {
      this.content = content;
      flag = true;
    }
    if (flag) {
      this.updatedAt = Instant.now();
    }
  }
}
