package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.binary.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.io.IOException;
import java.io.InputStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BinaryContentMapper {

  private final BinaryContentStorage binaryContentStorage;

  public BinaryContentDto toDto(BinaryContent entity) {
    if (entity == null) {
      return null;
    }
    String extension = getFileExtension(entity.getFileName());
    byte[] fileDate = convertInputStreamToByteArray(
        binaryContentStorage.get(entity.getId()));

    return new BinaryContentDto(
        entity.getId(),
        entity.getFileName(),
        entity.getSize(),
        entity.getContentType(),
        fileDate
    );

  }

  private String getFileExtension(String fileName) {
    int dotIndex = fileName.lastIndexOf(".");
    return (dotIndex > 0) ? fileName.substring(dotIndex) : "";
  }

  private byte[] convertInputStreamToByteArray(InputStream inputStream) {
    try {
      return inputStream.readAllBytes();
    } catch (IOException e) {
      throw new RuntimeException("파일 데이터를 읽는 중 오류 발생", e);
    }
  }
}
