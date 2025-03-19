package com.spirnt.mission.discodeit.dto.binaryContent;

import java.io.IOException;
import java.io.InputStream;
import org.springframework.web.multipart.MultipartFile;

public record BinaryContentCreateRequest(
    String fileName,
    String contentType,
    byte[] bytes
) {

  public BinaryContentCreateRequest(MultipartFile file) {
    this(file.getOriginalFilename(), file.getContentType(), getBytes(file));
  }

  // Stream 이용하기
  private static byte[] getBytes(MultipartFile file) {
    try (InputStream inputStream = file.getInputStream()) {
      return inputStream.readAllBytes();
    } catch (IOException e) {
      throw new RuntimeException("파일 변환 중 오류 발생", e);
    }
  }
}
