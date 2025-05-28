package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.binarycontetnt.BinaryContentResponse;
import jakarta.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class LocalBinaryContentStorage implements BinaryContentStorage {

  @Value("${discodeit.storage.local.root-path}")
  private String rootPath;

  private Path rootDirectory;

  @PostConstruct
  public void init() {
    rootDirectory = Paths.get(rootPath);
    if (!Files.exists(rootDirectory)) {
      try {
        Files.createDirectories(rootDirectory);
      } catch (IOException e) {
        throw new RuntimeException("Failed to create root directory", e);
      }
    }
  }

  @Override
  public UUID put(UUID uuid, byte[] data) throws IOException {
    Path filePath = resolvePath(uuid);
    Files.write(filePath, data, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    return uuid;
  }

  @Override
  public InputStream get(UUID uuid) throws IOException {
    Path filePath = resolvePath(uuid);
    if (!Files.exists(filePath)) {
      throw new FileNotFoundException("File not found: " + uuid);
    }
    return new FileInputStream(filePath.toFile());
  }

  @Override
  public ResponseEntity<?> download(BinaryContentResponse response) {
    try {
      Path filePath = resolvePath(response.id());

      if (!Files.exists(filePath)) {
        return ResponseEntity.notFound().build();
      }

      InputStreamResource resource = new InputStreamResource(
          new FileInputStream(filePath.toFile()));

      return ResponseEntity.ok()
          .header(HttpHeaders.CONTENT_DISPOSITION,
              "attachment; filename=\"" + response.fileName() + "\"")
          .contentType(MediaType.APPLICATION_OCTET_STREAM)
          .contentLength(response.size())
          .body(resource);
    } catch (IOException e) {
      return ResponseEntity.internalServerError().body("File could not be downloaded");
    }
  }

  private Path resolvePath(UUID uuid) {
    return rootDirectory.resolve(uuid.toString());
  }
}
