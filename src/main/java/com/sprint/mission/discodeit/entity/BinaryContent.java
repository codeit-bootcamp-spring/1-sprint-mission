package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "binary_contents")
public class BinaryContent extends BaseEntity {

  @Column(name = "file_name", nullable = false)
  private String fileName;
  @Column(nullable = false)
  private Long size;
  @Column(name = "content_type", nullable = false)
  private String contentType;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private UploadStatus uploadStatus;

  public void changeUploadStatus(UploadStatus status) {
    this.uploadStatus = status;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BinaryContent content = (BinaryContent) o;

    return Objects.equals(getId(), content.getId());

  }

  @Override
  public int hashCode() {
    return Objects.hash(getId());
  }
}
