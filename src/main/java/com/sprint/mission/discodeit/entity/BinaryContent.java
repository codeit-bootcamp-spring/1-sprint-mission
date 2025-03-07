package com.sprint.mission.discodeit.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class BinaryContent extends BaseEntity implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue
  private UUID id;

  private String fileName;
  private String mimeType;
  private String filePath;
  private Long size;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private Message message;

  @Lob // Binary 데이터 저장
  private byte[] bytes;

  @Builder
  public BinaryContent(String fileName, String mimeType, String filePath, byte[] bytes) {
    this.id = UUID.randomUUID();
    this.createdAt = Instant.now();
    this.fileName = fileName;
    this.mimeType = mimeType;
    this.filePath = filePath;
    this.bytes = bytes;
  }

  public BinaryContent(UUID fileId, String fileName, String mimeType, String filePath,
      byte[] bytes) {
    this.id = fileId;
    this.createdAt = Instant.now();
    this.fileName = fileName;
    this.mimeType = mimeType;
    this.filePath = filePath;
    this.bytes = bytes;
  }

  public BinaryContent(String fileName, String contentType, byte[] bytes) {
    this.id = UUID.randomUUID();
    this.createdAt = Instant.now();
    this.fileName = fileName;
    this.mimeType = contentType;
    this.bytes = bytes;
  }
}
