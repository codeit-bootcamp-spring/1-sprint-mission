package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "binary_contents")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BinaryContent extends BaseEntity implements Serializable {

  private static final Long serialVersionUID = 1L;

  @Column(name = "file_name")
  private String fileName; //file 경로 문자열

  @Column(name = "size")
  private Long size;

  @Column(name = "content_type")
  private String contentType;

  @Column(name = "bytes")
  private byte[] bytes;


}
