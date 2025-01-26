package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Channel implements Serializable {
  private static final long serialVersionUID = 1L;

  private UUID id;
  private Long createdAt;
  private Long updatedAt;
  private List<UUID> userIdList;
  private List<UUID> messageIdList;
  private String title;


  public Channel(String title) {
    this.id = UUID.randomUUID();
    this.createdAt = System.currentTimeMillis();
    this.title = title;
    this.updatedAt = createdAt;
    this.userIdList = new ArrayList<>();
    this.messageIdList = new ArrayList<>();
  }

  public Channel(Channel channel) {
    this.id = channel.id;
    this.createdAt = channel.createdAt;
    this.title = channel.title;
    this.updatedAt = channel.updatedAt;
    this.messageIdList = channel.messageIdList;
    this.userIdList = channel.userIdList;
  }

  public UUID getId() {
    return id;
  }


  public void updateTitle(String title) {
    this.title = title;
    this.updatedAt = System.currentTimeMillis();
  }

  public String getTitle() {
    return title;
  }

  public void addUser(UUID userId) {
    userIdList.add(userId);
  }

  public void addMessage(UUID messageId) {
    messageIdList.add(messageId);
  }

  public List<UUID> getUserIdList() {
    return userIdList;
  }

  public List<UUID> getMessageIdList() {
    return messageIdList;
  }


  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (obj instanceof Channel) {
      Channel channel = (Channel) obj;
      return Objects.equals(this.id, channel.id);
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
    return "Title : " + title + "/ createdAt : " + simpleDateFormat.format(createdAt)
        + " / updatedAt : " + updatedAt;
  }
}