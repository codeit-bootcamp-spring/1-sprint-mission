package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;

import lombok.NoArgsConstructor;

@Entity
@Table(name = "binary_contents")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class BinaryContent extends BaseEntity {

  @Column(nullable = false)
  private String fileName;

  @Column(nullable = false)
  private Long size;

  @Column(length = 100, nullable = false)
  private String contentType;

  @Column(nullable = false)
  private byte[] bytes;

  public static BinaryContent createBinaryContent(String fileName, Long size, String contentType,
      byte[] bytes) {
    return new BinaryContent(fileName, size, contentType, bytes);
  }

  private BinaryContent(String fileName, Long size, String contentType, byte[] bytes) {
    this.fileName = fileName;
    this.size = size;
    this.contentType = contentType;
    this.bytes = bytes;
  }
}
