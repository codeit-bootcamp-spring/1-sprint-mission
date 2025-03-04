package com.spirnt.mission.discodeit.dto.binaryContent;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public record BinaryContentCreateRequest(
    String fileName,
    String contentType,
    byte[] bytes
) {

  public BinaryContentCreateRequest(MultipartFile file) {
    this(file.getOriginalFilename(), file.getContentType(), getBytes(file));
  }

  private static byte[] getBytes(MultipartFile file) {
    try {
      return file.getBytes();
    } catch (IOException e) {
      throw new RuntimeException("파일 변환 중 오류 발생", e);
    }
  }
}
