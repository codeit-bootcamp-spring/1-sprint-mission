package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.NonNull;

import java.io.Serializable;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class Message implements Serializable {
  private static final long serialVersionUID = 1L;

  private final UUID id = UUID.randomUUID();
  private final Long createdAt = System.currentTimeMillis();
  private Long updatedAt = null;
  @NonNull
  private String content;
  private final UUID authorId;


  public void update(String content) {
    this.content = content;
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