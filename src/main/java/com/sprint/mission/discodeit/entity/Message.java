package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.exception.MessageValidationException;
import com.sprint.mission.discodeit.util.UuidGenerator;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

@Getter
@Setter
public class Message implements Serializable {
  private static final long serialVersionUID = 1L;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Message message = (Message) o;
    return Objects.equals(UUID, message.UUID);
  }

  @Override
  public int hashCode() {
    return Objects.hash(UUID);
  }

  private String UUID;
  private String userUUID;
  private String channelUUID;
  private String content;
  private Boolean isEdited;
  private Long createdAt;
  private Long updatedAt;
  private String contentImage;
  private String threadUUID;
  private Map<String, Reactions> messageReactions;

  private Message(MessageBuilder builder) {
    this.UUID = builder.UUID;
    this.userUUID = builder.userUUID;
    this.channelUUID = builder.channelUUID;
    this.content = builder.content;
    this.contentImage = builder.contentImage;
    this.threadUUID = builder.threadUUID;
    this.isEdited = builder.isEdited;
    this.createdAt = builder.createdAt;
    this.updatedAt = builder.updatedAt;
  }

  public static class MessageBuilder {
    private final String UUID;
    private final String userUUID;
    private final String channelUUID;
    private final String content;
    private Boolean isEdited = false;
    private String contentImage = "";
    private String threadUUID = "";
    private Long createdAt = System.currentTimeMillis();
    private Long updatedAt = System.currentTimeMillis();

    public MessageBuilder(String userUUID, String channelUUID, String content) throws MessageValidationException {
      if (userUUID == null || channelUUID == null || content == null) {
        throw new MessageValidationException();
      }
      this.UUID = UuidGenerator.generateUUID();
      this.userUUID = userUUID;
      this.channelUUID = channelUUID;
      this.content = content;
    }

    public MessageBuilder contentImage(String contentImage) {
      this.contentImage = contentImage == null ? "" : contentImage;
      return this;
    }

    public MessageBuilder threadUUID(String threadUUID) {
      this.threadUUID = threadUUID == null ? "" : threadUUID;
      return this;
    }

    public Message build() {
      return new Message(this);
    }
  }

  public void setContent(String content) {
    this.content = content;
    this.isEdited = true;
    this.updatedAt = System.currentTimeMillis();
  }

  public void setContentImage(String contentImage) {
    this.contentImage = contentImage == null ? "" : contentImage;
  }

  public void setIsEdited() {
    this.isEdited = true;
  }

  @Override
  public String toString() {
    return "Message{" +
        "UUID='" + UUID + '\'' +
        ", userUUID='" + userUUID + '\'' +
        ", channelUUID='" + channelUUID + '\'' +
        ", content='" + content + '\'' +
        ", contentImage='" + contentImage + '\'' +
        '}';
  }
}
