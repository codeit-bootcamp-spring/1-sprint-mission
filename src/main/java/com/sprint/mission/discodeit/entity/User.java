package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class User implements Serializable {
  private static final long serialVersionUID = 1L;

  private final UUID id;
  private final Long createdAt;
  private Long updatedAt;
  private final List<UUID> userMessageIdList;
  private final List<UUID> userChannelIdList;
  private String name;

  public User(String name) {
    this.id = UUID.randomUUID();
    this.createdAt = System.currentTimeMillis();
    this.name = name;
    this.updatedAt = createdAt;
    this.userMessageIdList = new ArrayList<>();
    this.userChannelIdList = new ArrayList<>();
  }

  public User(User user) {
    this.id = user.id;
    this.createdAt = user.createdAt;
    this.name = user.name;
    this.updatedAt = user.updatedAt;
    this.userMessageIdList = user.userMessageIdList;
    this.userChannelIdList = user.userChannelIdList;
  }

  public UUID getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void updateName(String name) {
    this.name = name;
    this.updatedAt = System.currentTimeMillis();
  }

  public void addMessage(UUID messageId) {
    userMessageIdList.add(messageId);
  }

  public void addChannel(UUID channelId) {
    userChannelIdList.add(channelId);
  }

  public List<UUID> getUserMessageIdList() {
    return userMessageIdList;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (obj instanceof User) {
      User user = (User) obj;
      return Objects.equals(this.id, user.id);
    }

    if (obj instanceof String) {
      return Objects.equals(this.id.toString(), obj);
    }

    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }


  public String toString() {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    return name + "/ createdAt = " + simpleDateFormat.format(createdAt) + "/ updatedAt="
        + updatedAt;

  }
}
