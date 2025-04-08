package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "channels")
public class Channel extends BaseUpdatableEntity {

  @Column(length = 100)
  private String name;

  @Column(length = 500)
  private String description;

  @Enumerated(EnumType.STRING)
  @Column(name = "type", nullable = false)
  private ChannelType channelType;


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

  @Builder
  public Channel(String name, String description, ChannelType channelType) {
    this.name = name;
    this.description = description;
    this.channelType = channelType;
  }


}