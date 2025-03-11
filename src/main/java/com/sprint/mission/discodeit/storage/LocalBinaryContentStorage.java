package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.config.StorageProperties;
import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import jakarta.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "local")
public class LocalBinaryContentStorage implements BinaryContentStorage {

  private final Path root;

  @Autowired
  public LocalBinaryContentStorage(StorageProperties storageProperties) {
    this.root = storageProperties.getRoot();
  }

  @Override
  public UUID put(UUID id, byte[] bytes) {
    Path filePath = resolvePath(id);

    try {
      Files.write(filePath, bytes);
    } catch (IOException e) {
      throw new RuntimeException("Failed saving file with path: " + root, e);
    }

    return id;
  }

  @Override
  public InputStream get(UUID id) {
    Path filePath = resolvePath(id);

    try {
      return new FileInputStream(filePath.toFile());
    } catch (FileNotFoundException e) {
      throw new NoSuchElementException("File not found with path: " + root, e);
    }
  }

  @Transactional(readOnly = true)
  @Override
  public ResponseEntity<?> download(BinaryContentDto binaryContentDto) {
    InputStream inputStream = get(binaryContentDto.id());

    return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType(binaryContentDto.contentType()))
        .header(HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"" + binaryContentDto.fileName() + "\"")
        .body(new InputStreamResource(inputStream));
  }

  @PostConstruct
  private void init() {
    try {
      Files.createDirectories(root);
    } catch (IOException e) {
      throw new RuntimeException("Failed creating directory with path: " + root, e);
    }
  }

  private Path resolvePath(UUID id) {
    return root.resolve(id.toString());
  }
}
