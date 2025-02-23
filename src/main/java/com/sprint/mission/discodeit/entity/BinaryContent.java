package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class BinaryContent implements Serializable {
  private final UUID id;
  private final Long createdAt;
  
  private UUID uploadedById;
  private String fileName;
  private Long size;
  private String contentType;
  private byte[] bytes;
  
  public BinaryContent(UUID uploadedById, String fileName, Long size, String contentType, byte[] bytes) {
    this.id = UUID.randomUUID();
    this.createdAt = Instant.now().getEpochSecond();
    
    this.uploadedById = uploadedById;
    this.fileName = fileName;
    this.size = size;
    this.contentType = contentType;
    this.bytes = bytes;
  }
}
