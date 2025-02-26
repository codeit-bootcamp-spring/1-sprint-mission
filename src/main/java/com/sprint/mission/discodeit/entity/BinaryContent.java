package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.global.util.TimeUtil;
import java.io.Serializable;
import java.time.Instant;
import java.util.Arrays;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class BinaryContent implements Serializable {

  private static final long serialVersionUID = 1L;
  private final UUID id;
  private String fileName;
  private String contentType;
  private final byte[] content;
  private final Instant createdAt;

  public static final BinaryContent EMPTY = new BinaryContent(
      "temFileName", "Text", new byte[0]);

  private BinaryContent(String fileName, String contentType, byte[] content) {
    this.id = UUID.randomUUID();
    this.fileName = fileName;
    this.contentType = contentType;
    this.content = content;
    this.createdAt = TimeUtil.getCurrentTime();
  }

  public static BinaryContent of(String fileName, String contentType, byte[] content) {
    return new BinaryContent(fileName, contentType, content);
  }

  @Override
  public String toString() {
    return "BinaryContent{" +
        "id=" + id +
        ", content=" + Arrays.toString(content) +
        ", createdAt=" + createdAt +
        '}';
  }
}
