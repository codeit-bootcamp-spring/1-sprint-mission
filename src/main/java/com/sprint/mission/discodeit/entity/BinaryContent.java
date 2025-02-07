package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.exception.RequiredFieldEmptyException;
import com.sprint.mission.discodeit.util.FileType;
import com.sprint.mission.discodeit.util.UuidGenerator;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.Arrays;
import java.util.Objects;

@Getter
public class BinaryContent implements Serializable {
  private static final long serialVersionUID = 1L;
  private final String UUID;
  private String channelId;
  private final String userId;
  private final String messageId;
  private final String fileName;
  private final FileType fileType;
  private final long fileSize;
  private final byte[] data;
  private final Instant createdAt;

  private BinaryContent(BinaryContentBuilder builder){
    this.UUID = UuidGenerator.generateUUID();
    this.userId = builder.userId;
    this.messageId = builder.messageId;
    this.channelId = builder.channelId;
    this.fileName = builder.fileName;
    this.fileType = builder.fileType;
    this.fileSize = builder.fileSize;
    this.data = builder.data;
    this.createdAt = Instant.now();
  }
  public static class BinaryContentBuilder {
    private final String userId;
    private String messageId;
    private String channelId;
    private final String fileName;
    private final FileType fileType;
    private final long fileSize;
    private final byte[] data;

    public BinaryContentBuilder(String userId, String fileName, FileType fileType, long fileSize, byte[] data) {
      this.userId = userId;
      this.fileName = fileName;
      this.fileType = fileType;
      this.fileSize = fileSize;
      this.data = data;
    }

    public BinaryContentBuilder messageId(String messageId) {
      this.messageId = messageId;
      return this;
    }

    public BinaryContentBuilder channelId(String channelId){
      this.channelId = channelId;
      return this;
    }

    public BinaryContent build() {
      return new BinaryContent(this);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    BinaryContent content = (BinaryContent) o;
    return Objects.equals(UUID, content.UUID);
  }

  @Override
  public int hashCode() {
    return Objects.hash(UUID);
  }

  @Override
  public String toString() {
    return "BinaryContent{" +
        "channelId='" + channelId + '\'' +
        ", userId='" + userId + '\'' +
        ", messageId='" + messageId + '\'' +
        ", fileName='" + fileName + '\'' +
        ", fileType=" + fileType +
        ", fileSize=" + fileSize +
        ", data=" + Arrays.toString(data) +
        '}';
  }
}
