package com.sprint.mission.discodeit.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "binary_contents")
//바이너리 데이터(이미지,파일)를 표현하는 도메인 모델
//사용자의 프로필 이미지,메시지에 첨부된 파일을 저장하기 위해 활용
public class BinaryContent extends Base implements Serializable {

  private static final long serialVersionUID = 1L;
  //메타 정보
  @Column(nullable = false)
  private String fileName; // 파일명

  @Column(nullable = false)
  private Long size; //파일 사이즈

  @Column(nullable = false, length = 100)
  private String contentType; // 타입

  public BinaryContent(String fileName, Long size, String contentType) {
    super();
    this.fileName = fileName;
    this.size = size;
    this.contentType = contentType;
  }
}

