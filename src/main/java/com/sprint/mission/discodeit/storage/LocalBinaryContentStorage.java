package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "local")
@Getter
public class LocalBinaryContentStorage implements BinaryContentStorage {

  private final Path root;

  public LocalBinaryContentStorage(
      @Value("${discodeit.storage.local.root-path}") String rootPath) {
    this.root = Paths.get(rootPath);
  }

  @PostConstruct
  public void init() throws IOException {
    if (!Files.exists(root)) {
      Files.createDirectories(root);
    }
  }

  @Override
  public UUID put(UUID id, byte[] data) {
    Path filePath = resolvePath(id);
    try {
      Files.write(filePath, data);
      return id;
    } catch (IOException e) {
      throw new RuntimeException("Failed to store binary content for id " + id, e);
    }
  }

  @Override
  public InputStream get(UUID id) {
    Path filePath = resolvePath(id);
    try {
      if (!Files.exists(filePath)) {
        throw new FileNotFoundException("Binary content with id " + id + " not found");
      }
      return Files.newInputStream(filePath);
    } catch (IOException e) {
      throw new RuntimeException("Failed to read binary content for id " + id, e);
    }
  }

  @Override
  public ResponseEntity<?> download(BinaryContentDto binaryContentDto) {
    try {
      InputStream inputStream = get(binaryContentDto.getId());
      InputStreamResource resource = new InputStreamResource(inputStream);

      HttpHeaders headers = new HttpHeaders();
      headers.add(HttpHeaders.CONTENT_DISPOSITION,
          "attachment; filename=" + binaryContentDto.getFileName());
      headers.setContentType(MediaType.parseMediaType(binaryContentDto.getContentType()));
      headers.setContentLength(binaryContentDto.getSize());

      return ResponseEntity.ok()
          .headers(headers)
          .body(resource);
    } catch (Exception e) {
      throw new RuntimeException(
          "Failed to download binary content for id " + binaryContentDto.getId(), e);
    }
  }

  @Override
  public void delete(UUID id) {
    Path filePath = resolvePath(id);
    try {
      if (Files.exists(filePath)) {
        Files.delete(filePath);
      }
    } catch (IOException e) {
      throw new RuntimeException("Failed to delete binary content for id " + id, e);
    }
  }

  private Path resolvePath(UUID id) {
    return root.resolve(id.toString());
  }
}