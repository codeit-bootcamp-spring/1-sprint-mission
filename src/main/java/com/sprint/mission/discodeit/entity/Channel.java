package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.global.error.execption.user.UserNotNullException;
import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class Channel extends BaseEntity {

  private String name;
  private String description;
  private final List<User> channelUserList = new ArrayList<>();
  private final ChannelType channelType;

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

  public void addChannelUser(User user) {
    if (user == null) {
      throw new UserNotNullException();
    }
    this.channelUserList.add(user);
  }

  public void removeUserFromChannel(User user) {
    if (user == null) {
      throw new UserNotNullException();
    }
    this.channelUserList.remove(user);
  }

  public boolean isPrivate() {
    return this.channelType == ChannelType.PRIVATE;
  }

  public boolean isPublic() {
    return this.channelType == ChannelType.PUBLIC;
  }

  public boolean isUserInChannel(User user) {
    return this.channelUserList.contains(user);
  }

  @Override
  public String toString() {
    return "Channel{" +
        "name='" + name + '\'' +
        ", channelUserList=" + channelUserList +
        '}';
  }
}
