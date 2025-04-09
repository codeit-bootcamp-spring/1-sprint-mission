package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.execption.ErrorCode;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import jakarta.annotation.PreDestroy;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "local")
public class LocalBinaryContentStorage implements BinaryContentStorage {

  private final Path root;

  public LocalBinaryContentStorage(Path root) {
    this.root = root;
    init();
  }

  private void init() {
    try {
      if (!Files.exists(root)) {
        Files.createDirectories(root);
      }
    } catch (IOException e) {
      throw new RuntimeException(ErrorCode.DIRECTORY_INIT_FAIL.format("path: " + root));
    }
  }

  @Override
  public UUID put(UUID id, byte[] content) {
    save(resolvePath(id), content);
    return id;
  }

  @Override
  public InputStream get(UUID id) {
    return load(root, id);
  }

  @Override
  public ResponseEntity<Resource> download(BinaryContentDto binaryContentDto) {
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"" + binaryContentDto.fileName() + "\"")
        .body(new InputStreamResource(get(binaryContentDto.id())));
  }

  private Path resolvePath(UUID id) {
    return root.resolve(id.toString());
  }

  private <T> void save(Path filePath, T data) {
    try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(filePath))) {
      oos.writeObject(data);
    } catch (IOException e) {
      throw new RuntimeException(ErrorCode.FILE_WRITE_FAIL
          .format("path: " + filePath + "data: " + data));
    }
  }

  public InputStream load(Path directory, UUID id) {
    try {
      if (!Files.exists(directory)) {
        return null;
      }
      try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory)) {
        for (Path path : stream) {
          try {
            File file = path.toFile();
            String fileName = file.getName();
            if (!fileName.equals(id.toString())) {
              continue;
            }
            return new FileInputStream(file);
          } catch (IOException e) {
            throw new RuntimeException(ErrorCode.FILE_READ_FAIL.format("path: " + path.toString()));
          }
        }
      }
    } catch (IOException e) {
      throw new RuntimeException(ErrorCode.FILES_LOAD_FAIL.format("path: " + directory));
    }
    return null;
  }

  @PreDestroy
  public void clearDataDirectory() {
    try {
      if (Files.exists(root)) {
        Files.walk(root)
            .sorted((a, b) -> b.compareTo(a)) // 파일부터 삭제 후 디렉토리 삭제
            .forEach(path -> {
              try {
                Files.deleteIfExists(path);
              } catch (IOException e) {
                throw new RuntimeException(ErrorCode.FILE_REMOVE_FAIL
                    .format("path: " + path.toString()));
              }
            });
      }
    } catch (IOException e) {
      throw new RuntimeException(ErrorCode.FILES_LOAD_FAIL.format("path: " + root));
    }
  }
}
