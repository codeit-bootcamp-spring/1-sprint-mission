package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.exception.RequiredFieldEmptyException;
import com.sprint.mission.discodeit.util.FileType;
import com.sprint.mission.discodeit.util.UuidGenerator;
import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.Arrays;
import java.util.Objects;

@Getter
@Setter
public class BinaryContent implements Serializable {
  private static final long serialVersionUID = 1L;
  private final String UUID;
  @Nullable
  private String channelId;
  private final String userId;
  @Nullable
  private final String messageId;
  private final String fileName;
  private final String fileType;
  private final long fileSize;
  private final byte[] data;
  private final boolean isProfilePicture;
  private final Instant createdAt;
  public BinaryContent(String userId,
                        String messageId,
                        String channelId,
                        String fileName,
                        String fileType,
                        long fileSize,
                        byte[] data,
                        boolean isProfilePicture){
    this.UUID = UuidGenerator.generateUUID();
    this.userId = userId;
    this.messageId = messageId;
    this.channelId = channelId;
    this.fileName = fileName;
    this.fileType = fileType;
    this.fileSize = fileSize;
    this.data = data;
    this.isProfilePicture = isProfilePicture;
    this.createdAt = Instant.now();
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
