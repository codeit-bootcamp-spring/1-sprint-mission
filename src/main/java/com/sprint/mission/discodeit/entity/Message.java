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

  private String id;
  private String authorId;
  private String channelId;
  private String content;
  private Boolean isEdited;
  private Instant createdAt;
  private Instant updatedAt;
  private List<String> attachmentIds;

  public static class MessageBuilder{
    private String id =  UuidGenerator.generateid();
    private Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();
    private boolean isEdited = false;
    public String getId(){
      return id;
    }
  }

  public void setContent(String content) {
    this.content = content;
    this.isEdited = true;
    this.updatedAt = Instant.now();
  }

  public void addBinaryContents(List<String> binaryContents){
    this.attachmentIds = binaryContents;
    updatedAt = Instant.now();
  }

  public void addBinaryContent(String binaryContent) {
    this.attachmentIds.add(binaryContent);
    updatedAt = Instant.now();
  }

  public void removeBinaryContent(String  binaryContent) {
    this.attachmentIds.remove(binaryContent);
    updatedAt = Instant.now();
  }

  public void setIsEdited() {
    this.isEdited = true;
  }

  @Override
  public String toString() {
    return "Message{"
        + "id='" + id + '\''
        + ", userid='" + authorId + '\''
        + ", channelid='" + channelId + '\''
        + ", content='" + content + '\''
        + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Message message = (Message) o;
    return Objects.equals(id, message.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
