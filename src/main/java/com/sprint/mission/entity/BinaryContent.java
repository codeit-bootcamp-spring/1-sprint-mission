package com.sprint.mission.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Entity
@EqualsAndHashCode(of = {"fileName", "contentType", "size"}, callSuper = true)
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@ToString(of = {"fileName", "size", "contentType"})
@Getter
@Schema(description = "바이너리 컨텐츠")
@Table(name = "binary_contents")
public class BinaryContent extends BaseEntity {

  private String fileName;
  private Long size;
  private String contentType;

  @OneToOne(mappedBy = "profile")
  private User user;

  public BinaryContent(String fileName, Long size, String contentType) {
    this.fileName = fileName;
    this.size = size;
    this.contentType = contentType;
  }
}