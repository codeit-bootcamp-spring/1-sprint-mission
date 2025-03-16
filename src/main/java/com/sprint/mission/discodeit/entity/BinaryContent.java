package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

// TODO JPA 위해서는 @NoArgsConstructor로 기본생성자 필요 -> final 못 씀 but 파라미터 전달로 객체생성해줘야하니까
// TODO @AllArgsConstructor, @Builder 쓰기 -> @Builder는 원하는 필드의 파라미터만 전달해서 객체 생성 가능
// TODO 아니면 그냥 파라미터 생성자는 롬복쓰지말고 직접 만들어주기..


@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
// TODO찐 : JPA에서 기본생성자 필수, protected 이유 : proxy..?
@Entity
@Table(name = "binary_contents")
public class BinaryContent extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  @Column(name = "file_name", nullable = false, length = 255)
  private String fileName;
  @Column(nullable = false)
  private int size;
  @Column(name = "content_type", nullable = false, length = 100)
  private String contentType;
}
