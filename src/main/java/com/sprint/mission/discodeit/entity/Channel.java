package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "channels")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = true)
public class Channel extends BaseUpdatableEntity {

  @Column(length = 100)
  private String name;

  @Column(length = 500)
  private String description;

  @Column(name = "type", nullable = false, length = 10)
  @Enumerated(EnumType.STRING)
  private ChannelType channelType;

  private Channel(String name, String description, ChannelType channelType) {
    super();
    this.name = name;
    this.description = description;
    this.channelType = channelType;
  }

  public static Channel of(String name, String description, ChannelType channelType) {
    return new Channel(name, description, channelType);
  }

  public void updateName(String name) {
    this.name = name;
    this.updateUpdatedAt();
  }

  public void updateDescription(String description) {
    this.description = description;
    this.updateUpdatedAt();
  }

  public void updateChannelInfo(String name, String description) {
    this.updateName(name);
    this.updateDescription(description);
  }

  public boolean isPrivate() {
    return this.channelType == ChannelType.PRIVATE;
  }

  public boolean isPublic() {
    return this.channelType == ChannelType.PUBLIC;
  }
}
