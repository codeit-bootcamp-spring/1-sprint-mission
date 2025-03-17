package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.config.StorageConfig;
import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(value = "discodeit.storage.type", havingValue = "local")
@Slf4j
public class LocalBinaryContentStorage implements BinaryContentStorage {

  private final Path root;

  public LocalBinaryContentStorage(StorageConfig config) {
    this.root = config.getRootPath();
    log.info("root path: {}", this.root.toAbsolutePath());
  }

  @PostConstruct
  public void init() {
    try {
      Files.createDirectories(root);
    } catch (IOException e) {
      throw new RuntimeException("Could not initialize storage", e);
    }
  }

  @Override
  public UUID put(UUID id, byte[] bytes) {
    Path filePath = resolvePath(id);
    try {
      Files.write(filePath, bytes);
      return id;
    } catch (IOException e) {
      throw new RuntimeException("Could not store file", e);
    }
  }

  @Override
  public InputStream get(UUID id) {
    Path filePath = resolvePath(id);
    try {
      return Files.newInputStream(filePath);
    } catch (IOException e) {
      throw new RuntimeException("Could not read file", e);
    }
  }


  @Override
  public ResponseEntity<Resource> download(BinaryContentDto binaryContentDto) {
    Path filePath = resolvePath(binaryContentDto.id());

    if (!Files.exists(filePath)) {
      log.error("File not found: {}", filePath);
      return ResponseEntity.notFound().build();
    }

    try {
      Resource resource = new InputStreamResource(Files.newInputStream(filePath));
      return ResponseEntity.ok()
          .contentType(MediaType.parseMediaType(binaryContentDto.contentType()))
          .header(HttpHeaders.CONTENT_DISPOSITION,
              "attachment; filename=\"" + binaryContentDto.fileName() + "\"")
          .body(resource);
    } catch (IOException e) {
      throw new RuntimeException("Could not download file", e);
    }
  }


  private Path resolvePath(UUID id) {
    return root.resolve(id.toString());
  }
}