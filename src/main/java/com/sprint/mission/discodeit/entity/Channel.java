package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Getter
public class Channel extends BaseEntity implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;
  // 추가할 수 있는 것 : 채널에 소속(참가)된 사용자
  // 채널의 속성 : private, public
  // private 의 경우 owner의 초대로만 참가할 수 있다.
  // public 의 경우 1. 1.1서버의 주소/1.2서버 서칭 탭에서 서칭을 통한 접근을 통해 스스로 참가 가능하다. 2. owner의 초대로 참가
  private ChannelType type; //추가
  private UUID ownerId;
  private String name;
  private String description;

  public Channel(ChannelType type, UUID ownerId, String name, String description) {
    if (name == null) {
      throw new IllegalArgumentException("channelName 은 null일 수 없습니다.");
    }
    this.type = type;
    this.ownerId = ownerId;
    this.name = name;
    this.description = description;
  }

  public void updateName(String name) {
    if (name == null) {
      throw new IllegalArgumentException("channelName 은 null일 수 없습니다.");
    }
    this.name = name;
  }

  public void updateDescription(String description) {
    // description 은 공백일 수 있다고 가정
    if (description == null) {
      throw new IllegalArgumentException("description 은 null일 수 없습니다.");
    }
    this.description = description;
  }
}
