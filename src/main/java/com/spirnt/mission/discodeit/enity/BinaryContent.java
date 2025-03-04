package com.spirnt.mission.discodeit.enity;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;

// 이미지, 파일 등 바이너리 데이터를 표현하는 도메인 모델
@Getter
public class BinaryContent implements Serializable {

  private static final long serialVersionUID = 1L;
  private UUID id;
  private Instant createdAt;
  private String fileName;
  private Long size;
  private String contentType;
  private byte[] bytes;

  public BinaryContent(String fileName, Long size, String contentType, byte[] bytes) {
    this.id = UUID.randomUUID();
    this.createdAt = Instant.now();
    //
    this.fileName = fileName;
    this.size = size;
    this.contentType = contentType;
    this.bytes = bytes;
  }

}
