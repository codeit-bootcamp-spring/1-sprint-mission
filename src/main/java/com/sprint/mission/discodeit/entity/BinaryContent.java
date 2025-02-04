package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.exception.RequiredFieldEmptyException;
import com.sprint.mission.discodeit.util.FileType;
import com.sprint.mission.discodeit.util.UuidGenerator;

import java.io.Serializable;
import java.time.Instant;

public class BinaryContent implements Serializable {
  private static final long serialVersionUID = 1L;
  private final String UUID;
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
    this.fileName = builder.fileName;
    this.fileType = builder.fileType;
    this.fileSize = builder.fileSize;
    this.data = builder.data;
    this.createdAt = Instant.now();
  }
  public static class BinaryContentBuilder {
    private final String userId;
    private String messageId;
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

    public BinaryContent build() {
      return new BinaryContent(this);
    }
  }
}
