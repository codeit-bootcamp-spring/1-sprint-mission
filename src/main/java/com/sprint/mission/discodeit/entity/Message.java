package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class Message implements Serializable {
  private static final long serialVersionUID = 1L;

  private final UUID id = UUID.randomUUID();
  private final Instant createdAt = Instant.now();
  private Instant updatedAt = null;
  @NonNull
  private String content;
  private final UUID authorId;
  private final UUID channelId;
  private final List<UUID> attachmentIds;


  public void update(String newContent) {
    boolean anyValueUpdated = false;
    if (newContent != null && !newContent.equals(this.content)) {
      this.content = newContent;
      anyValueUpdated = true;
    }

    if (anyValueUpdated) {
      this.updatedAt = Instant.now();
    }
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