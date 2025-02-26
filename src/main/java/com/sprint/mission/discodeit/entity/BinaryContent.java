package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.util.UuidGenerator;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.Arrays;
import java.util.Objects;

@Getter

@Builder(toBuilder = true)
@AllArgsConstructor
public class BinaryContent implements Serializable {
  private static final long serialVersionUID = 1L;
  private final String id;
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

  public static class BinaryContentBuilder{
    private String id = UuidGenerator.generateid();
    private Instant createdAt = Instant.now();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    BinaryContent content = (BinaryContent) o;
    return Objects.equals(id, content.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
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
