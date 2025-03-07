package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class Message extends BaseUpdatableEntity implements Serializable {

  private static final long serialVersionUID = 1L;
  //객체 식별 id

  //메세지 작성자
  private final String authorId;
  //메세지 내용
  private String content;
  //메세지가 생성된 채널
  private final String channelId;
  //첨부 이미지 목록
  private final List<String> attachmentImageIds;

  public Message(String authorId, String content, String channelId) {
    this.authorId = authorId;
    this.content = content;
    this.channelId = channelId;
    this.attachmentImageIds = new ArrayList<>();
  }

  public void setContent(String content) {
    this.content = content;
  }

  //메세지가 생성된 이후, 생성 시간을 변경할 수 없으므로 update 미구현

  //메세지가 생성된 이후, 메세지를 보낸 채널을 변경할 수 없으므로 update 미구현

  public void addImages(String imageId) {
    this.attachmentImageIds.add(imageId);
  }

  //추후에 추가할 것
  //멘션, 답장(reply)

}
