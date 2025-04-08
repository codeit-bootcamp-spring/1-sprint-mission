package com.sprint.mission.discodeit.storage.local;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "local")
@Component
public class LocalBinaryContentStorage implements BinaryContentStorage {

  private final Path root;

  public LocalBinaryContentStorage(
      @Value("${discodeit.storage.local.root-path}") Path root
  ) {
    this.root = root;
  }

  @PostConstruct
  public void init() {
    log.info("[파일 초기화 시도]");

    if (!Files.exists(root)) {
      try {
        log.info("[파일 저장 경로 생성 시도]");
        Files.createDirectories(root);
      } catch (IOException e) {
        log.error("[파일 저장 경로 생성 실패]", e);
        throw new RuntimeException(e);
      }
    }
  }

  public UUID put(UUID binaryContentId, byte[] bytes) {
    Path filePath = resolvePath(binaryContentId);
    log.info("[파일 저장 시도] 파일 ID: {}", binaryContentId);

    if (Files.exists(filePath)) {
      log.error("[파일 저장 실패] 해당 파일이 이미 존재합니다. 파일 ID: {}", binaryContentId);
      throw new IllegalArgumentException("File with key " + binaryContentId + " already exists");
    }

    try (OutputStream outputStream = Files.newOutputStream(filePath)) {
      outputStream.write(bytes);
      log.info("[파일 저장 성공] 파일 ID: {}", binaryContentId);
    } catch (IOException e) {
      log.error("[파일 저장 실패] 파일 ID: {}}", binaryContentId, e);
      throw new RuntimeException(e);
    }
    return binaryContentId;
  }

  public InputStream get(UUID binaryContentId) {
    Path filePath = resolvePath(binaryContentId);
    log.info("[파일 변환 시도] 파일 ID: {}", binaryContentId);

    if (Files.notExists(filePath)) {
      log.error("[파일 조회 실패] 해당 파일을 찾을 수 없습니다. 파일 ID: {}", binaryContentId);
      throw new NoSuchElementException("File with key " + binaryContentId + " does not exist");
    }
    try {
      log.info("[파일 변환 성공] 파일 ID: {}", binaryContentId);
      return Files.newInputStream(filePath);
    } catch (IOException e) {
      log.error("[파일 변환 실패] 파일을 InputStream 타입으로 변환 실패했습니다. 파일 ID: {}", binaryContentId, e);
      throw new RuntimeException(e);
    }
  }

  private Path resolvePath(UUID key) {
    return root.resolve(key.toString());
  }

  @Override
  public ResponseEntity<Resource> download(BinaryContentDto metaData) {
    InputStream inputStream = get(metaData.id());
    Resource resource = new InputStreamResource(inputStream);
    log.info("[파일 다운로드 시도] 파일 ID: {}", metaData.id());

    return ResponseEntity
        .status(HttpStatus.OK)
        .header(HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"" + metaData.fileName() + "\"")
        .header(HttpHeaders.CONTENT_TYPE, metaData.contentType())
        .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(metaData.size()))
        .body(resource);
  }
}
