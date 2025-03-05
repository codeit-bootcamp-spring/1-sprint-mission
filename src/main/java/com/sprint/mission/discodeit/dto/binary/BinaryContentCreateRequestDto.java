package com.sprint.mission.discodeit.dto.binary;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Getter
public class BinaryContentCreateRequestDto {

  String fileName;
  String contentType;
  byte[] bytes;

  public BinaryContentCreateRequestDto(String fileName, String contentType, byte[] bytes) {
    this.fileName = fileName;
    this.contentType = contentType;
    this.bytes = bytes;
  }
}
