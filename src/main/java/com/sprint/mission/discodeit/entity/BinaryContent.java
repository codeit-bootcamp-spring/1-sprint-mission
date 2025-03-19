package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(name = "binary_contents")
public class BinaryContent extends BaseEntity /*implements Serializable */ {

  @Column
  private String fileName;

  @Column
  private Long size;

  @Column
  private String contentType;

  @Column(nullable = false)
  private byte[] bytes;

  protected BinaryContent() {
    super();
  }

  public BinaryContent(String fileName, Long size, String contentType, byte[] bytes) {

    // super(); // BaseEntity의 생성자 호출하여 ID 생성 없어도 호출이 가능하나 명시적으로 작성
    this.fileName = fileName;
    this.size = size;
    this.contentType = contentType;
    this.bytes = bytes;
  }

}
