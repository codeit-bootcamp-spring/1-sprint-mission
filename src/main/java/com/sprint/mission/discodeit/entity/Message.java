package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import lombok.Getter;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Message extends BaseUpdatableEntity {


  private String content;
  private UUID authorId;
  private UUID channelId;
  private List<UUID> attachmentIds;

  public Message(String content, UUID userId, UUID channelId) {
    this.content = content;
    this.authorId = userId;
    this.channelId = channelId;
    attachmentIds = new ArrayList<>();
  }


  public void setMessage(String content) {
    if (content != null && !content.equals(this.content)) {
      this.content = content;
    } else {
      throw new IllegalArgumentException("입력한 메시지: " + content + "가 기존 값과 같습니다.");
    }
  }

  public void addBinaryContent(UUID binaryContentId) {
    attachmentIds.add(binaryContentId);
  }


}