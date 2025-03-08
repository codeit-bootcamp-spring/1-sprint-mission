package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
public class BinaryContent implements Serializable {

  private static final Long serialVersionUID = 1L;

  @Id
  private UUID id;

  private Instant createdAt;

  private String filePath; //file 경로 문자열

  //TODO: Sprint 3 image or file 구분하는 필드  -> 멀티패스파일?

  public BinaryContent() {
  }

  public BinaryContent(String filePath) {
    this.id = UUID.randomUUID();
    this.createdAt = Instant.now();
    this.filePath = filePath;
  }


}
