package com.sprint.mission.discodeit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;
import java.io.Serializable;
import lombok.NoArgsConstructor;
import jakarta.persistence.Table;


@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "channels")
public class Channel extends BaseUpdatableEntity implements Serializable {

  private static final long serialVersionUID = 1L; //직렬화 버전

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ChannelType type;

  @Column(length = 100)
  private String name; // 채널 이름

  @Column(length = 500)
  private String description; // 채널 설명

  public Channel(ChannelType type, String name, String description) {
    super();
    //검증
    if (type == null) {
      throw new IllegalArgumentException("채널 타입은 필수입니다.(PUBLIC,PRIVATE)");
    }
//    if (name.trim().isEmpty() || name.length() > 30) {
//      throw new IllegalArgumentException("채널 이름은 1~30자 사이여야 합니다.");
//    }
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
