package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@NoArgsConstructor
public class BinaryContentMapper {

  protected BinaryContentStorage binaryContentStorage;

  protected BinaryContentDto toDto(BinaryContent binaryContent) {

    // TODO InputStream은 사용 후 반드시 닫아야 하므로 try-with-resources 구문 사용
    try (InputStream fis = binaryContentStorage.get(binaryContent.getId())) {
      byte[] data = fis.readAllBytes(); // readAllBytes(): 반환값으로 byte 배열을 반환
      return new BinaryContentDto(
          binaryContent.getId(),
          binaryContent.getFileName(),
          binaryContent.getSize(),
          binaryContent.getContentType()
      );
    } catch (IOException e) {
      throw new RuntimeException("파일을 읽는 중 오류 발생", e);
    }
  }
}