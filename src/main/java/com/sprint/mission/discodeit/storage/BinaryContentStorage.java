package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

@Slf4j
public interface BinaryContentStorage {

  UUID put(UUID binaryContentId, byte[] bytes);

  InputStream get(UUID binaryContentId);

  ResponseEntity<?> download(BinaryContentDto metaData);

  void put(UUID id, byte[] bytes) throws IOException;

  byte[] get(UUID id) throws IOException;

  void delete(UUID id) throws IOException;

  default void put(UUID id, InputStream inputStream) throws IOException {
    log.info("파일 업로드 시작 - fileId: {}", id);
    try {
      byte[] bytes = inputStream.readAllBytes();
      put(id, bytes);
      log.info("파일 업로드 완료 - fileId: {}, size: {} bytes", id, bytes.length);
    } catch (IOException e) {
      log.error("파일 업로드 실패 - fileId: {}, error: {}", id, e.getMessage());
      throw e;
    }
  }

  default byte[] get(UUID id) throws IOException {
    log.debug("파일 다운로드 시작 - fileId: {}", id);
    try {
      byte[] bytes = getBytes(id);
      log.debug("파일 다운로드 완료 - fileId: {}, size: {} bytes", id, bytes.length);
      return bytes;
    } catch (IOException e) {
      log.error("파일 다운로드 실패 - fileId: {}, error: {}", id, e.getMessage());
      throw e;
    }
  }

  default void delete(UUID id) throws IOException {
    log.info("파일 삭제 시작 - fileId: {}", id);
    try {
      deleteFile(id);
      log.info("파일 삭제 완료 - fileId: {}", id);
    } catch (IOException e) {
      log.error("파일 삭제 실패 - fileId: {}, error: {}", id, e.getMessage());
      throw e;
    }
  }

  byte[] getBytes(UUID id) throws IOException;

  void deleteFile(UUID id) throws IOException;
}
