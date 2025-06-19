package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.configure.UploadStatus;
import com.sprint.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "binary_contents")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BinaryContent extends BaseEntity {

  @Column(nullable = false, name = "file_name")
  private String fileName;
  @Column(nullable = false)
  private Long size;
  @Column(length = 100, nullable = false, name = "content_type")
  private String contentType;
  @Setter
  @Enumerated(EnumType.STRING)
  private UploadStatus uploadStatus = UploadStatus.WAITING;
  @Getter
  @Setter
  @Transient
  private byte[] fileData;

  public BinaryContent(String fileName, Long size, String contentType) {
    this.fileName = fileName;
    this.size = size;
    this.contentType = contentType;
  }
}
