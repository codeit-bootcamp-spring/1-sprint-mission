package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.binary.BinaryContentDto;
import com.sprint.mission.discodeit.exception.file.FileUploadFailedException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.*;
import java.nio.file.*;
import java.util.UUID;


@Conditional(LocalStorageCondition.class)
@RequiredArgsConstructor
public class LocalBinaryContentStorage implements BinaryContentStorage {

  private final Path root;

  public LocalBinaryContentStorage(@Value("${discodeit.storage.local.root-path}") String rootPath) {
    this.root = Paths.get(rootPath);
  }

  @EventListener(ApplicationReadyEvent.class)
  public void init() {
    try {
      Files.createDirectories(root);
    } catch (IOException e) {
      throw new RuntimeException("Could not initialize local binary content storage", e);
    }
  }

  @Override
  public UUID put(UUID id, byte[] data) {
    String fileName = id.toString();

    Path filePath = resolvePath(fileName);
    try {
      Files.write(filePath, data);
      return null;
    } catch (IOException e) {
      throw new FileUploadFailedException();
    }
  }

  @Override
  public InputStream get(UUID id) {
    Path filePath = resolvePath(id.toString());
    try {
      return Files.newInputStream(filePath);
    } catch (IOException e) {
      throw new RuntimeException("Failed to read file: " + filePath, e);
    }
  }

  @Override
  public ResponseEntity<?> download(BinaryContentDto binaryContentDto) {
    InputStream inputStream = get(binaryContentDto.getId());
    Resource resource = new InputStreamResource(inputStream);
    if (!resource.exists()) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity
        .status(HttpStatus.OK)
        .header(HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"" + binaryContentDto.getFileName() + "\"")
        .header(HttpHeaders.CONTENT_TYPE, binaryContentDto.getContentType())
        .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(binaryContentDto.getSize()))
        .body(resource);
  }

  private Path resolvePath(String fileName) {
    return root.resolve(fileName);
  }
}
