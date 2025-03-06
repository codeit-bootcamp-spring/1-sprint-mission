package com.sprint.mission.discodeit.converter;

import com.sprint.mission.discodeit.dto.request.binary.BinaryContentCreateRequest;
import java.io.IOException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class ToBinaryContentConverter {

  public BinaryContentCreateRequest convert(MultipartFile file) {
    if (file == null || file.isEmpty()) {
      return null;
    }
    try {
      return new BinaryContentCreateRequest(
          file.getOriginalFilename(),
          file.getContentType(),
          file.getBytes()
      );
    } catch (IOException exception) {
      throw new RuntimeException("파일 변환중 오류 발생", exception);
    }
  }
}
