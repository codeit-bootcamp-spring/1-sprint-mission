package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Table(name = "binary_contents")
@Entity
@AllArgsConstructor
@Builder
public class BinaryContent extends BaseEntity {

  // 메타데이터
  @Column(nullable = false, length = 255) // PostgreSQL : 길이를 지정하지 않으면 TEXT로 인식되고, 무제한 글자 수가 저장 가능
  private String fileName;

  @Column(nullable = false)
  private Long size;

  @Column(nullable = false, length = 100)
  private String contentType;

  // JPA용 기본 생성자, JPA만 접근할 수 있도록 protected 접근자 설정
  protected BinaryContent() {
  }

}
