package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
  @Column
  private uploadStatus status;


  public BinaryContent(String fileName, Long size, String contentType) {
    this.fileName = fileName;
    this.size = size;
    this.contentType = contentType;
    this.status = uploadStatus.WAITING;
  }

  public void updateStatus(uploadStatus status) {
    this.status = status;
  }

  public enum uploadStatus {
    WAITING,
    SUCCESS,
    FAILED
  }
}
