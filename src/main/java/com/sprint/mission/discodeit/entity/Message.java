package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.util.UuidGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
public class Message implements Serializable {
  private static final long serialVersionUID = 1L;


  private String UUID;
  private String userId;
  private String channelId;
  private String content;
  private Boolean isEdited;
  private Instant createdAt;
  private Instant updatedAt;
  private List<BinaryContent> binaryContents;

  public static class MessageBuilder{
    private String UUID =  UuidGenerator.generateUUID();
    private Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();
    private boolean isEdited = false;
    public String getUUID(){
      return UUID;
    }
  }

  public void setContent(String content) {
    this.content = content;
    this.isEdited = true;
    this.updatedAt = Instant.now();
  }

  public void addBinaryContents(List<BinaryContent> binaryContents){
    this.binaryContents = binaryContents;
    updatedAt = Instant.now();
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
    return "Message{"
        + "UUID='" + UUID + '\''
        + ", userUUID='" + userId + '\''
        + ", channelUUID='" + channelId + '\''
        + ", content='" + content + '\''
        + '}';
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
