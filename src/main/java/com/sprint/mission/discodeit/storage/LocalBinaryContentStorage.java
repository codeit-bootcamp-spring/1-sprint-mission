package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.binary.BinaryContentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.*;
import java.util.UUID;

@Component
@Conditional(LocalStorageCondition.class)
@RequiredArgsConstructor
public class LocalBinaryContentStorage implements BinaryContentStorage {

  private final Path root;

  @Autowired
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
  public Path put(UUID id, byte[] data, String extension) {
    System.out.println("put 호출: " + id);

    // 🔹 확장자가 "."으로 시작하는지 확인하고 없으면 추가
    String safeExtension = formatExtension(extension);
    String fileName = id.toString() + safeExtension;

    Path filePath = resolvePath(fileName);
    try {
      Files.write(filePath, data);
      System.out.println("✅ 파일이 로컬에 저장됨: " + filePath);
      return filePath;
    } catch (IOException e) {
      throw new RuntimeException("Failed to store file", e);
    }
  }

  @Override
  public InputStream get(UUID id, String extension) {
    String safeExtension = formatExtension(extension);
    Path filePath = resolvePath(id.toString() + safeExtension);

    try {
      return Files.newInputStream(filePath);
    } catch (IOException e) {
      throw new RuntimeException("Failed to read file: " + filePath, e);
    }
  }

  @Override
  public ResponseEntity<?> download(BinaryContentDto binaryContentDto, String extension) {
    String safeExtension = formatExtension(extension);
    Path filePath = resolvePath(binaryContentDto.getId().toString() + safeExtension);

    Resource resource = new FileSystemResource(filePath);
    if (!resource.exists()) {
      return ResponseEntity.notFound().build();
    }

    String contentType = binaryContentDto.getContentType();
    if (contentType == null || contentType.isBlank()) {
      contentType = "application/octet-stream";
    }
    return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType(contentType))
        .header(HttpHeaders.CONTENT_DISPOSITION,
            "inline; filename=\"" + binaryContentDto.getFileName() + "\"")
        .body(resource);
  }

  @Override
  public boolean exists(UUID id, String extension) {
    String safeExtension = formatExtension(extension);
    return Files.exists(resolvePath(id.toString() + safeExtension));
  }

  @Override
  public void delete(UUID id, String extension) {
    String safeExtension = formatExtension(extension);
    try {
      Files.deleteIfExists(resolvePath(id.toString() + safeExtension));
    } catch (IOException e) {
      throw new RuntimeException("Failed to delete file", e);
    }
  }

  private Path resolvePath(String fileName) {
    return root.resolve(fileName);
  }

  // 🔹 확장자 "."이 없으면 자동으로 추가하는 메서드
  private String formatExtension(String extension) {
    if (extension == null || extension.isEmpty()) {
      return "";
    }
    return extension.startsWith(".") ? extension : "." + extension;
  }
}
