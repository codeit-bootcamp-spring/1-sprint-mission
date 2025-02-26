package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Getter
public class Message extends BaseEntity implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;
  //
  private UUID channelId;
  private UUID authorId;
  private String content;
  private List<UUID> attachmentIds;

  public Message(UUID channelId, UUID authorId, String content, List<UUID> attachmentIds) {
    // 공백일 수 있다.
    if (content == null) {
      throw new IllegalArgumentException("messageText 은 null 일 수 없습니다.");
    }
    this.channelId = channelId;
    this.authorId = authorId;
    this.content = content;
    this.attachmentIds = attachmentIds;
  }

  public void updateMessageText(String content) {
    if (content == null) {
      throw new IllegalArgumentException("channelName 은 null 일 수 없습니다.");
    }
    this.content = content;
    this.refreshUpdateAt();
  }
}
