package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.constant.ChannelType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class Channel extends BaseEntity implements Serializable {
  private static final long serialVersionUID = 1L;
  private final UUID ownerId;
  private ChannelType type;
  private String name;
  private String description;
  private List<UUID> memberIds;
  
  // public channel
  public Channel(UUID ownerId, String name, String description) {
    this.name = name;
    this.description = description;
    this.type = ChannelType.PUBLIC;
    this.ownerId = null;
  }
  
  // private channel
  public Channel(UUID ownerId, List<UUID> memberIds) {
    this.ownerId = ownerId;
    this.type = ChannelType.PRIVATE;
    this.memberIds = memberIds;
  }
  
  public static Channel ofPublic(UUID ownerId, String name, String description) {
    return new Channel(ownerId, name, description);
  }
  
  public static Channel ofPrivate(UUID ownerId, List<UUID> memberIds) {
    return new Channel(ownerId, memberIds);
  }
  
  public void updateName(String name) {
    this.name = name;
    super.update();
  }
  
  public void updateMembers(List<UUID> memberIds) {
    this.memberIds = memberIds;
    super.update();
  }
  
}
