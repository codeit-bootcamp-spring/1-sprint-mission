package com.sprint.mission.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.*;

@Entity
@EqualsAndHashCode(of = {"channelType", "name"}, callSuper = true)
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@ToString(of = {"channelType", "name", "description"})
@Getter
@Schema(description = "채널")
@Table(name = "channels")
public class Channel extends BaseUpdatableEntity {

  private ChannelType channelType;
  private String name;
  private String description;

  @OneToMany(mappedBy = "channel", cascade = REMOVE, orphanRemoval = true)
  private List<ReadStatus> readStatus = new ArrayList<>();

  public Channel(String name, String description, ChannelType channelType) {
    this.name = name;
    this.channelType = channelType;
    this.description = description;
  }

  public void update(String newName, String newDescription) {
    this.name = newName;
    this.description = newDescription;
  }

  public boolean isPrivate() {
    if (this.channelType == ChannelType.PRIVATE) {
      return true;
    } else {
      return false;
    }
  }
}