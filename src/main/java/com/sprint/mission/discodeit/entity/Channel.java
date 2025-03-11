package com.sprint.mission.discodeit.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "channels")
@Getter
@NoArgsConstructor
public class Channel extends BaseUpdatableEntity {

  @Column(nullable = false, length = 100)
  private String name;

  @Column(length = 500)
  private String description;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 10)
  private ChannelType type;

  @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Message> messages;

  public Channel(ChannelType type, String name, String description) {
    this.type = type;
    this.name = name;
    this.description = description;
  }

  public static Channel ofPublic(String name, String description) {
    return new Channel(ChannelType.PUBLIC, name, description);
  }

  public static Channel ofPrivate() {
    return new Channel(ChannelType.PRIVATE, null, null);
  }

  public void updateName(String newName) {
    this.name = newName;
    onUpdate();
  }

  public void updateDescription(String newDescription) {
    this.description = newDescription;
    onUpdate();
  }

  public enum ChannelType {
    PUBLIC,
    PRIVATE
  }
}
