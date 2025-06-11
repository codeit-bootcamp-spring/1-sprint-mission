package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import com.sprint.mission.discodeit.entity.status.BinaryContentUploadStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;

import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@Table(name = "binary_contents")
@NoArgsConstructor
public class BinaryContent extends BaseEntity {

  @Column(name = "file_name")
  private String fileName;

  @Column(nullable = false)
  private String contentType;

  private long size;

  @Setter
  @Enumerated(EnumType.STRING)
  private BinaryContentUploadStatus uploadStatus;

  public BinaryContent(String fileName, String contentType, long size,
      BinaryContentUploadStatus uploadStatus) {
    this.fileName = fileName;
    this.contentType = contentType;
    this.size = size;
    this.uploadStatus = uploadStatus;
  }
}
