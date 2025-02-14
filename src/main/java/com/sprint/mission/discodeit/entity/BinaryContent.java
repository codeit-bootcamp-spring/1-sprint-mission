package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.constant.BinaryContentType;
import lombok.Getter;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class BinaryContent implements Serializable {
  private final UUID id;
  private final Long createdAt;
  
  private final UUID uploadedById;
  private final BinaryContentType type;
  
  public BinaryContent(UUID uploadedById, BinaryContentType type) {
    this.id = UUID.randomUUID();
    this.createdAt = Instant.now().getEpochSecond();
    this.uploadedById = uploadedById;
    this.type = type;
  }
}
