package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "channels")
public class Channel extends BaseUpdatableEntity {

  @Column(length = 100)
  private String name;

  @Column(length = 500)
  private String description;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ChannelType type;

  private Channel(String name, String description, ChannelType type) {
    this.name = name;
    this.description = description;
    this.type = type;
  }

  public static Channel publicChannel(String name, String description) {
    return new Channel(name, description, ChannelType.PUBLIC);
  }

  public static Channel privateChannel() {
    return new Channel(null, null, ChannelType.PRIVATE);
  }

  public void update(String newName, String newDescription) {
    this.name = newName;
    this.description = newDescription;
  }

  public boolean isPublic() {
    return type.equals(ChannelType.PUBLIC);
  }

  public enum ChannelType {
    PRIVATE, PUBLIC
  }
}
