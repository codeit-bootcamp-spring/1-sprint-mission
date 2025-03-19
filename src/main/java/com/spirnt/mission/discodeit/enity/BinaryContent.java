package com.spirnt.mission.discodeit.enity;

import com.spirnt.mission.discodeit.enity.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 이미지, 파일 등 바이너리 데이터를 표현하는 도메인 모델
@Entity
@Table(name = "binary_contents")
@NoArgsConstructor
@Getter
public class BinaryContent extends BaseEntity {

  private String fileName;
  private Long size;
  private String contentType;

  public BinaryContent(String fileName, Long size, String contentType) {
    super();
    this.fileName = fileName;
    this.size = size;
    this.contentType = contentType;
  }

  // UUID만으로 객체를 비교하기 위해 추가
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    BinaryContent binaryContent = (BinaryContent) obj;
    return Objects.equals(this.getId(), binaryContent.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.getId());
  }

}
