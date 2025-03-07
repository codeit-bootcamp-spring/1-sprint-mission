package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class BinaryContent extends BaseEntity {

  private static final long serialVersionUID = 1L;
  private String filename;
  private byte[] binaryImage;
  private String contentType;
  private long size;

  public BinaryContent(String filename, byte[] binaryImage, String contentType, long size) {
    this.filename = filename;
    this.binaryImage = binaryImage;
    this.contentType = contentType;
    this.size = size;
  }
}
