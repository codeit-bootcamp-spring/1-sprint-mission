package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.response.BinaryContentDto;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.binary.FailReadBinaryContent;
import com.sprint.mission.discodeit.exception.binary.NotSavedBinaryContentException;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "local")
public class LocalBinaryContentStorage implements BinaryContentStorage {

  private final Path path;

  public LocalBinaryContentStorage(@Value("${discodeit.storage.local.root-path}") String path) {
    this.path = Paths.get(path);
  }

  @PostConstruct
  public void init() {
    try {
      Files.createDirectories(path);
    } catch (IOException e) {
      throw new RuntimeException("local storage init fail: ", e);
    }
  }

  @Override
  public Path resolvePath(UUID id) {
    return path.resolve(id.toString());
  }

  @Override
  public UUID put(UUID fileId, byte[] bytes) {
    Path filePath = resolvePath(fileId);
    try {
      Files.write(filePath, bytes);
      log.info("파일 저장: {}", fileId);
    } catch (IOException e) {
      log.error("파일 저장 실패: {}", fileId);
      throw new NotSavedBinaryContentException(Instant.now(), ErrorCode.NOT_SAVED_FILE,
          Map.of("fileId", ErrorCode.NOT_SAVED_FILE.getMessage())
      );
    }

    return fileId;
  }

  @Override
  public InputStream get(UUID fileId) {
    Path filePath = resolvePath(fileId);

    try {
      return Files.newInputStream(filePath);
    } catch (IOException e) {
      log.error("파일 읽기 실패: {}", fileId);
      throw new FailReadBinaryContent(Instant.now(), ErrorCode.FAIL_READ_FILE,
          Map.of("fileId", ErrorCode.FAIL_READ_FILE.getMessage()));
    }

  }

  @Override
  public ResponseEntity<?> download(BinaryContentDto file) {
    InputStream findFileStream = get(file.id());

    InputStreamResource fileResource = new InputStreamResource(findFileStream);
    log.info("파일 다운로드: {}", file.fileName());

    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.fileName() + "\"")
        .contentLength(file.size())
        .contentType(MediaType.parseMediaType(file.contentType()))
        .body(fileResource);
  }
}
