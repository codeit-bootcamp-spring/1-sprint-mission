package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Message implements Serializable {

  private static final long serialVersionUID = 1L;
  private final UUID id;
  private final UUID channelId;
  private UUID authorId; // 이미 변경됨
  private String content;
  private final Instant createdAt;
  private Instant updatedAt;
  private List<UUID> attachmentIds;

  public Message(UUID authorId, UUID channelId, String content) {
    this.id = UUID.randomUUID();
    this.authorId = authorId;
    this.channelId = channelId;
    this.content = content;
    this.createdAt = Instant.now();
    this.updatedAt = Instant.now();
    this.attachmentIds = new ArrayList<>();
  }

  public Message(UUID authorId, UUID channelId, String content, List<UUID> attachmentIds) {
    this.id = UUID.randomUUID();
    this.authorId = authorId;  // senderId에서 authorId로 변경
    this.channelId = channelId;
    this.content = content;
    this.createdAt = Instant.now();
    this.updatedAt = Instant.now();
    this.attachmentIds = attachmentIds != null ? attachmentIds : new ArrayList<>();
  }

  public void updateContent(String newContent) {
    this.content = newContent;
    this.updatedAt = Instant.now();
  }

  public void addAttachment(UUID attachmentId) {
    if (this.attachmentIds == null) {
      this.attachmentIds = new ArrayList<>();
    }
    this.attachmentIds.add(attachmentId);
    this.updatedAt = Instant.now();
  }
}