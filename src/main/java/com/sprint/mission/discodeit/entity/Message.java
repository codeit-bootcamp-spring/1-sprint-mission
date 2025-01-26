package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Objects;
import java.util.UUID;

public class Message implements Serializable {
  private static final long serialVersionUID = 1L;

  private UUID id;
  private Long createdAt;
  private Long updatedAt;
  private UUID userId;
  private UUID channelId;
  private String content;

  public Message(UUID userId, String content, UUID channelId) {
    this.id = UUID.randomUUID();
    this.createdAt = System.currentTimeMillis();
    this.userId = userId;
    this.content = content;
    this.channelId = channelId;
    this.updatedAt = createdAt;
  }

  public Message(Message message) {
    this.id = message.id;
    this.createdAt = message.createdAt;
    this.userId = message.userId;
    this.content = message.content;
    this.channelId = message.channelId;
    this.updatedAt = message.updatedAt;
  }

  public UUID getId() {
    return id;
  }

  public UUID getUserId() {
    return userId;
  }

  public boolean isUserEqual(UUID getUser) {
    return userId.equals(getUser);
  }

  public boolean isChannelEqual(UUID getChannel) {
    return channelId.equals(getChannel);
  }


  public void updateMessage(String message) {
    this.content = message;
    this.updatedAt = System.currentTimeMillis();
  }

  public UUID getChannelId() {
    return channelId;
  }

  public String getContent() {
    return content;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (obj instanceof Message) {
      Message message = (Message) obj;
      return Objects.equals(this.id, message.id);
    }

    if (obj instanceof String) {
      return Objects.equals(this.id.toString(), obj);
    }

    return false;
  }

<<<<<<< HEAD
  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
=======
    public UUID getUserId() {
        return userId;
    }

    public boolean isUserEqual(UUID userId) {
        return userId.equals(getUser);
    }

    public boolean isChannelEqual(UUID channelId) {
        return channelId.equals(getChannel);
    }


    public void updateId(UUID id) {
        this.id = id;
    }
    public void updateCreatedAt(Long CreatedAt) {
        this.createdAt = createdAt;
    }

    public void updateMessage(String message){
        this.content = message;
        this.updatedAt = System.currentTimeMillis();
    }
    public UUID getChannelId() {
        return channelId;
    }

    // 메세지 넣기

    public String toString() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return userName + "/ createdAt : " + simpleDateFormat.format(createdAt) + "\n" + "[ " + content
                +" ]";

    }
>>>>>>> 5206eed33a9c5cfc572bc8a1095473360a1463b3
}
