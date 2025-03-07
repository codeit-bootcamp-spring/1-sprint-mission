package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;

import java.time.Instant;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "channels")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Channel extends BaseUpdatableEntity {

  @Enumerated(EnumType.STRING)
  private ChannelType type;

  @Column(length = 100, unique = true, nullable = false)
  private String name;

  @Column(length = 500, unique = true, nullable = false)
  private String description;

  public enum ChannelType {
    PUBLIC,
    PRIVATE
  }

  public static Channel createChannel(ChannelType channelType, String title, String description) {
    return new Channel(channelType, title, description);
  }

  private Channel(ChannelType type, String title, String description) {
    this.name = title;
    this.description = description;
    this.type = type;
  }

  public void update(String newTitle, String newDescription) {
    boolean isChanged = false;
    if (!newTitle.equals(this.name)) {
      this.name = newTitle;
      isChanged = true;
    }
    if (!newDescription.equals(this.description)) {
      this.description = newDescription;
      isChanged = true;
    }
    if (isChanged) {
      this.updatedAt = Instant.now();
    }
  }

  @Override
  public String toString() {
    return "Channel{" +
        "type=" + type +
        ", name='" + name + '\'' +
        ", description='" + description + '\'' +
        '}';
  }
}
