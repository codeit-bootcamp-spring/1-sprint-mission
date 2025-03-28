package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import jakarta.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "local")
public class LocalBinaryContentStorage implements BinaryContentStorage {

  private final Path rootPath;

  public LocalBinaryContentStorage(@Value("${discodeit.storage.local.root-path}") String rootPath) {
    this.rootPath = Paths.get(rootPath);
    init();
  }

  @PostConstruct // 빈 생성시 자동으로 호출되어 저장소 디렉터리 구조를 준비
  private void init() {
    log.info("Initializing storage directory {}", rootPath);
    try {
      if (!Files.exists(this.rootPath)) {
        Files.createDirectories(this.rootPath);
      }
      log.info("Initialized storage directory {}", this.rootPath);
    } catch (IOException e) {
      throw new RuntimeException("Failed to initialized storage directory", e);
    }
  }

  //파일의 실제 저장 위치에 대한 규칙을 정의하고 일관된 파일 경로 규칙을 유지하기 위한 메소드
  private Path resolvePath(UUID id) {
    if (id == null) {
      throw new IllegalArgumentException("Content id cannot be null");
    }
    return rootPath.resolve(id.toString());
  }

  @Override
  @Transactional
  public UUID put(UUID id, byte[] content) {
    log.info("파일 저장: id = {}", id);
    try {
      Path filePath = resolvePath(id);
      Files.write(filePath, content);
      log.info("파일 저장 완료");
      return id;
    } catch (IOException e) {
      log.error("파일 쓰기 오류: {}", e.getMessage());
      throw new RuntimeException("Failed to store file", e);
    }
  }

  @Override
  @Transactional(readOnly = true)
  public InputStream get(UUID id) {
    try {
      Path filePath = resolvePath(id);
      return new FileInputStream(filePath.toFile());
    } catch (IOException e) {
      log.error("파일 읽기 오류: {}", e.getMessage());
      throw new RuntimeException("Failed to read file", e);
    }
  }

  @Override
  @Transactional(readOnly = true)
  public ResponseEntity<Resource> download(BinaryContentDto binaryContentDto) {
    log.info("파일 다운로드: binaryContentId = {}", binaryContentDto.id());
    try {
      InputStream fileStream = get(binaryContentDto.id());
      InputStreamResource resource = new InputStreamResource(fileStream);

      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.parseMediaType(binaryContentDto.contentType()));

      String filename = binaryContentDto.fileName() + "." + binaryContentDto.contentType();

      headers.setContentDisposition(
          ContentDisposition.builder("attachment")
              .filename(filename, StandardCharsets.UTF_8)
              .build());
      if (binaryContentDto.size() != null) {
        headers.setContentLength(binaryContentDto.size());
      }
      return ResponseEntity.ok().headers(headers).body(resource);
    } catch (Exception e) {

      // 오류 메시지를 StringResource로 변환하여 Resource 타입으로 반환
      log.error("파일 다운로드 실패: {}", e.getMessage());
      ByteArrayResource errorResource = new ByteArrayResource(
          ("Failed to download file: " + e.getMessage()).getBytes());
      throw new RuntimeException("Failed to download file", e);
    }
  }
}
