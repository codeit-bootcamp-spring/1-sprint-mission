package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.exception.MessageValidationException;
import com.sprint.mission.discodeit.util.UuidGenerator;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Getter
@Setter
public class Message implements Serializable {
  private static final long serialVersionUID = 1L;


  private final String UUID;
  private final String userId;
  private final String channelId;
  private String content;
  private Boolean isEdited;
  private final Instant createdAt;
  private Instant updatedAt;
  private List<BinaryContent> binaryContents = new ArrayList<>();

  public Message(
      String userId,
      String channelId,
      String content,
      List<BinaryContent> binaryContents
      ) {
    this.UUID = UuidGenerator.generateUUID();
    this.userId = userId;
    this.channelId = channelId;
    this.content = content;
    this.binaryContents = binaryContents;
    this.isEdited = false;
    this.createdAt = Instant.now();
    this.updatedAt = Instant.now();
  }

  public void setContent(String content) {
    this.content = content;
    this.isEdited = true;
    this.updatedAt = Instant.now();
  }

  public void addBinaryContent(BinaryContent binaryContent) {
    this.binaryContents.add(binaryContent);
    updatedAt = Instant.now();
  }

  public void removeBinaryContent(BinaryContent binaryContent) {
    this.binaryContents.remove(binaryContent);
    updatedAt = Instant.now();
  }

  public void setIsEdited() {
    this.isEdited = true;
  }

  @Override
  public String toString() {
    return "Message{" +
        "UUID='" + UUID + '\'' +
        ", userUUID='" + userId + '\'' +
        ", channelUUID='" + channelId + '\'' +
        ", content='" + content + '\'' +
        '}';
  }

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
}
