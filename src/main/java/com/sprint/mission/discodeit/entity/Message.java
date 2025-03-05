package com.sprint.mission.discodeit.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Message implements Serializable {

  private static final long serialVersionUID = 1L;

  private final UUID id = UUID.randomUUID();
  private final Instant createdAt = Instant.now();
  private Instant updatedAt;

  private String content;
  private UUID authorId;
  private UUID channelId;
  private List<UUID> attachmentIds;


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