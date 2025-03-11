package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.LocalStorageCondition;
import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import jakarta.annotation.PostConstruct;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Conditional(LocalStorageCondition.class)
public class LocalBinaryContentStorage implements BinaryContentStorage {

  private final Path root;

  public LocalBinaryContentStorage(@Value("${discodeit.storage.local.root-path}") String rootPath) {
    this.root = Paths.get(rootPath).toAbsolutePath().normalize();
  }

  @PostConstruct
  public void init() {
    try {
      Files.createDirectories(root);
      log.info("Local storage directory initialized at: {}", root);
    } catch (IOException e) {
      throw new RuntimeException("Failed to create storage root directory", e);
    }
  }


  @Override
  public UUID put(UUID id, byte[] data) {
    UUID fileId = (id != null) ? id : UUID.randomUUID();
    Path filePath = resolvePath(fileId);

    try {
      Files.write(filePath, data, StandardOpenOption.CREATE_NEW);
      log.info(" File stored successfully: {}", filePath);
    } catch (IOException e) {
      throw new RuntimeException(" Failed to store file", e);
    }

    return fileId;
  }

  @Override
  public InputStream get(UUID id) {
    Path filePath = resolvePath(id);

    try {
      return new BufferedInputStream(Files.newInputStream(filePath));
    } catch (IOException e) {
      throw new RuntimeException(" Failed to retrieve file", e);
    }
  }

  @Override
  public ResponseEntity<?> download(BinaryContentDto binaryContentDto) {
    UUID storageId = binaryContentDto.id();
    Path filePath = resolvePath(storageId);

    if (!Files.exists(filePath)) {
      log.warn("File does not exist: {}", filePath);
      return ResponseEntity.notFound().build();
    }

    try {

      Resource resource = new FileSystemResource(filePath);

      return ResponseEntity.ok()
          .header(HttpHeaders.CONTENT_DISPOSITION,
              "attachment; filename=\"" + binaryContentDto.fileName() + "\"")
          .header(HttpHeaders.CONTENT_TYPE, binaryContentDto.contentType())
          .body(resource);

    } catch (Exception e) {
      throw new RuntimeException(" Failed to download file", e);
    }
  }


  private Path resolvePath(UUID id) {
    return root.resolve(id.toString());
  }
}
