package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.Table;


@Entity
@Table(name = "channels")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Channel extends BaseUpdatableEntity {

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ChannelType type;

  @Column(length = 100)
  private String name; // 채널 이름

  @Column(length = 500)
  private String description; // 채널 설명

  public Channel(ChannelType type, String name, String description) {
    if (type == null) {
      throw new IllegalArgumentException("채널 타입은 필수입니다.(PUBLIC,PRIVATE)");
    }
    if (description != null && description.length() > 100) {
      throw new IllegalArgumentException("채널 설명은 최대 100자까지 가능합니다.");
    }
    this.type = type;
    this.name = name;
    this.description = description;
  }

  // update 메소드
  public void updateChannel(String newName, String newDescription) {
    if (newName != null && !newName.trim().isEmpty() && !newName.equals(
        this.name)) {
      this.name = newName;
      updateTimestamp();
    }
    if (newDescription != null && !newDescription.trim().isEmpty() && !newDescription.equals(
        this.description)) {
      this.description = newDescription;
      updateTimestamp();
    }
  }

}
