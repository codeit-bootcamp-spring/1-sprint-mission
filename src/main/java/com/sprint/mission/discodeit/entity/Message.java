package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.exception.MessageValidationException;
import com.sprint.mission.discodeit.util.UuidGenerator;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
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
  private final Instant createdAt;
  private Instant updatedAt;
  private String binaryContentId; // TODO: 삭제
  private String threadUUID;

  private Message(MessageBuilder builder) {
    this.UUID = builder.UUID;
    this.userUUID = builder.userUUID;
    this.channelUUID = builder.channelUUID;
    this.content = builder.content;
    this.binaryContentId = builder.binaryContentId;
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
    private String binaryContentId = "";
    private String threadUUID = "";
    private final Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();

    public MessageBuilder(String userUUID, String channelUUID, String content) throws MessageValidationException {
      if (userUUID == null || channelUUID == null || content == null) {
        throw new MessageValidationException();
      }
      this.UUID = UuidGenerator.generateUUID();
      this.userUUID = userUUID;
      this.channelUUID = channelUUID;
      this.content = content;
    }

    public MessageBuilder binaryContentId(String binaryContentId) {
      this.binaryContentId = binaryContentId == null ? "" : binaryContentId;
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
    this.updatedAt = Instant.now();
  }

  public void setContentImage(String binaryContentId) {
    this.binaryContentId = binaryContentId == null ? "" : binaryContentId;
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
        ", contentImage='" + binaryContentId + '\'' +
        '}';
  }
}
