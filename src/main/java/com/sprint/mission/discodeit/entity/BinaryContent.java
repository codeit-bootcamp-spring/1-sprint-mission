package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class BinaryContent extends BaseEntity {

  private byte[] bytes;  // 바이너리 데이터 저장
  private String contentType;
  private Long size;
  private String fileName;


  public BinaryContent(byte[] bytes, String fileName, String contentType, Long size) {
    this.fileName = fileName;
    this.bytes = bytes;
    this.contentType = contentType;
    this.size = size;
  }

}
