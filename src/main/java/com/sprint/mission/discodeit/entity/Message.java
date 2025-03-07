package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class Message extends BaseUpdatableEntity {

  //메세지 작성자
  private final User author;
  //메세지 내용
  private String content;
  //메세지가 생성된 채널
  private final Channel channel;
  //첨부 이미지 목록
  private final List<BinaryContent> attachments;

  public Message(User author, String content, Channel channel) {
    this.author = author;
    this.content = content;
    this.channel = channel;
    this.attachments = new ArrayList<>();
  }

  public void setContent(String content) {
    this.content = content;
  }

  public void addFile(BinaryContent binaryContent) {
    this.attachments.add(binaryContent);
  }

  //추후에 추가할 것
  //멘션, 답장(reply)

}
