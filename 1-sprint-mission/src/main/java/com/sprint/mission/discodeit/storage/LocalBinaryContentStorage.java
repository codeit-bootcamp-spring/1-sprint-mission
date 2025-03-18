package com.sprint.mission.discodeit.storage;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
@ConditionalOnProperty(value = "discodeit.storage.type", havingValue = "local") // 변경된 부분
public class LocalBinaryContentStorage implements BinaryContentStorage {

  private final Path root;

  public LocalBinaryContentStorage(@Value("${discodeit.storage.local.root-path}") String rootPath) {
    this.root = Paths.get(rootPath).toAbsolutePath().normalize();
  }

  @PostConstruct
  public void init() {
    try {
      Files.createDirectories(root);
    } catch (IOException e) {
      throw new RuntimeException("Failed to create storage root directory", e);
    }
  }


  @Override
  public UUID put(UUID id, byte[] data) {
    UUID fileId = (id != null) ? id : UUID.randomUUID();
    Path filePath = resolvePath(fileId);

    try {
      Files.write(filePath, data);
      return fileId;
    } catch (IOException e) {
      throw new RuntimeException(" Failed to store file", e);
    }
  }

  @Override
  public InputStream get(UUID id) {
    Path filePath = resolvePath(id);

    try {
      return Files.newInputStream(filePath);
    } catch (IOException e) {
      throw new RuntimeException(" Failed to retrieve file", e);
    }
  }

  @Override
  public ResponseEntity<?> download(UUID id) {
    Path filePath = resolvePath(id);

    if (!Files.exists(filePath)) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    try {
      InputStream inputStream = get(id);
      String contentType = Files.probeContentType(filePath);
      if (contentType == null) {
        contentType = "application/octet-stream";
      }

      long fileSize = Files.size(filePath);
      String fileName = filePath.getFileName().toString();
      Resource resource = new InputStreamResource(inputStream);

      return ResponseEntity.ok()
          .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
          .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(fileSize))
          .contentType(MediaType.parseMediaType(contentType))
          .body(resource);

    } catch (Exception e) {
      throw new RuntimeException(" Failed to download file", e);
    }
  }


  private Path resolvePath(UUID id) {
    return root.resolve(id.toString());
  }
}
