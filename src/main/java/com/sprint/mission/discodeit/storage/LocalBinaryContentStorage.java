package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "local")
public class LocalBinaryContentStorage implements BinaryContentStorage {

  private final Path root;

  public LocalBinaryContentStorage(@Value("${discodeit.storage.local.root-path}") String rootPath) {
    this.root = Paths.get(rootPath).toAbsolutePath().normalize();
  }

  @PostConstruct
  public void init() {
    if (!Files.exists(root)) {
      try {
        Files.createDirectories(root);
      } catch (IOException e) {
        e.printStackTrace();
        throw new RuntimeException(e);
      }
    }
  }


  @Override
  public UUID put(UUID id, byte[] data) {

    if (id == null) {
      id = UUID.randomUUID();
    }

    Path filePath = resolvePath(id);
    log.debug("파일 저장 요청 - id: {}, 경로: {}", id, filePath);
    if (Files.exists(filePath)) {
      log.warn("파일 저장 중단 - 이미 존재하는 파일: {}", filePath);
      throw new IllegalArgumentException("File with key " + id + " already exists");
    }
    try (OutputStream outputStream = Files.newOutputStream(filePath)) {
      outputStream.write(data);
      log.debug("파일 저장 완료 - id: {}, 크기: {} bytes", id, data.length);
    } catch (IOException e) {
      log.error("파일 저장 실패 - id: {}, 경로: {}", id, filePath, e);
      throw new RuntimeException(e);
    }
    return id;
  }

  @Override
  public InputStream get(UUID id) {
    Path filePath = resolvePath(id);
    if (Files.notExists(filePath)) {
      throw new NoSuchElementException("File with key " + id + " does not exist");
    }
    try {
      return Files.newInputStream(filePath);
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  @Override
  public ResponseEntity<?> download(BinaryContentDto metaData) {
    log.debug("파일 다운로드 요청 - id: {}, 파일명: {}", metaData.id(), metaData.fileName());

    InputStream inputStream = get(metaData.id());
    Resource resource = new InputStreamResource(inputStream);

    return ResponseEntity
        .status(HttpStatus.OK)
        .header(HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"" + metaData.fileName() + "\"")
        .header(HttpHeaders.CONTENT_TYPE, metaData.contentType())
        .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(metaData.size()))
        .body(resource);
  }


  private Path resolvePath(UUID id) {
    return root.resolve(id.toString());
  }
}
