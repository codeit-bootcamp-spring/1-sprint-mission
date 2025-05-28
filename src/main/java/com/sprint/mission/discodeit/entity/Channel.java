package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "channels")
@Getter
@AllArgsConstructor
@Builder
public class Channel extends BaseUpdatableEntity {

  @Column(nullable = false, length = 10)
  @Enumerated(EnumType.STRING)
  private ChannelType type;

  @Column(length = 100)
  private String name;

  @Column(length = 500)
  private String description;

  // JPA 리플랙션을 위한 기본 생성자
  protected Channel() {
  }

  public void updateName(String newName) {
    if (name == null) {
      throw new IllegalArgumentException("channelName 은 null일 수 없습니다.");
    }
    this.name = newName;
  }

  public void updateDescription(String newDescription) {
    if (description == null) {
      throw new IllegalArgumentException("description 은 null일 수 없습니다.");
    }
    this.description = newDescription;
  }
}
