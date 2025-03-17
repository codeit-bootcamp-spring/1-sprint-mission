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

  private UUID id;

  private String fileName;
  private String mimeType;
  private String filePath;
  private Long size;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private Message message;

  @Builder
  public BinaryContent(String fileName, String mimeType, String filePath) {
    this.createdAt = Instant.now();
    this.fileName = fileName;
    this.mimeType = mimeType;
    this.filePath = filePath;
  }

  public BinaryContent(String fileName, String contentType) {
    this.createdAt = Instant.now();
    this.fileName = fileName;
    this.mimeType = contentType;
  }

  public BinaryContent(UUID fileId, String fileName, String mimeType, String filePath) {
    this.id = fileId;
    this.createdAt = Instant.now();
    this.fileName = fileName;
    this.mimeType = mimeType;
    this.filePath = filePath;
  }
}
