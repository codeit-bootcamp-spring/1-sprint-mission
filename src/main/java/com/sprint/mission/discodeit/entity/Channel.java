package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Channel extends BaseUpdatableEntity {

  private String name;
  private String description;
  private ChannelType type;

  public Channel(String name, String description, ChannelType type) {
    this.name = name;
    this.description = description;
    this.type = type;
  }

  public void setChannel(String name, String description) {
    if (name != null && !name.equals(this.name)) {
      this.name = name;
    } else {
      throw new IllegalArgumentException("입력한 채널이름: " + name + "이 기존 값과 같습니다.");
    }

    if (description != null && !description.equals(this.description)) {
      this.description = description;
    } else {
      throw new IllegalArgumentException("입력한 설명: " + description + "이 기존 값과 같습니다.");
    }
  }


}