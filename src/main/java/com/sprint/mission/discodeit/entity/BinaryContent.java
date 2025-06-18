package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "binary_contents")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BinaryContent extends BaseEntity {

  @Column(nullable = false)
  private String fileName;
  @Column(nullable = false)
  private Long size;
  @Column(length = 100, nullable = false)
  private String contentType;

  @Enumerated(EnumType.STRING)
  private BinaryContentUploadStatus uploadStatus;

  public BinaryContent(String fileName, Long size, String contentType) {
    this.fileName = fileName;
    this.size = size;
    this.contentType = contentType;
  }
  public void markWaiting() {
    this.uploadStatus = BinaryContentUploadStatus.WAITING;
  }

  public void markSuccess() {
    this.uploadStatus = BinaryContentUploadStatus.SUCCESS;
  }

  public void markFailed() {
    this.uploadStatus = BinaryContentUploadStatus.FAILED;
  }

  public boolean isFailed() {
    return this.uploadStatus == BinaryContentUploadStatus.FAILED;
  }



}
