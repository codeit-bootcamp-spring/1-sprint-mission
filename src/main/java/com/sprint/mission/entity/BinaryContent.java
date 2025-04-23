package com.sprint.mission.entity;

import static lombok.AccessLevel.*;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Entity
@EqualsAndHashCode(of = {"fileName", "contentType", "size"}, callSuper = true)
@NoArgsConstructor(access = PROTECTED)
@ToString(of = {"fileName", "size", "contentType"})
@Getter
@Schema(description = "바이너리 컨텐츠")
@Table(name = "binary_contents")
public class BinaryContent extends BaseEntity {

  @Column(nullable = false)
  private String fileName;
  @Column(nullable = false)
  private Long size;
  @Column(length = 100, nullable = false)
  private String contentType;

  public BinaryContent(String fileName, Long size, String contentType) {
    this.fileName = fileName;
    this.size = size;
    this.contentType = contentType;
  }
}