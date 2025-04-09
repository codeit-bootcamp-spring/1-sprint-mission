package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentDto;
import jakarta.annotation.PostConstruct;
import java.io.OutputStream;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.UUID;
import org.springframework.stereotype.Component;

@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "local") // 조건부 Bean으로 등록
@Component
public class LocalBinaryContentStorage implements BinaryContentStorage {

  private final Path root;

  // 설정 정보를 외부에서 주입
  public LocalBinaryContentStorage(@Value("${discodeit.storage.local.root-path}") Path root) {
    this.root = root;
  }

  @PostConstruct //Bean 등록 후 자동으로 호출되는 메소드를 정의
  public void init() { //Path root 디렉토리 초기화
    try {
      Files.createDirectories(root);
    } catch (IOException e) {
      throw new RuntimeException("파일 생성 실패", e);
    }
  }

  @Override
  public UUID put(UUID binaryContentId, byte[] bytes) {
    Path filePath = resolvePath(binaryContentId);
    if (Files.exists(filePath)) {
      throw new IllegalArgumentException("File with key " + binaryContentId + " already exists");
    }
    try (OutputStream outputStream = Files.newOutputStream(filePath)) {
      outputStream.write(bytes);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return binaryContentId;
  }

  @Override
  public InputStream get(UUID binaryContentId) {
    Path filePath = resolvePath(binaryContentId);
    if (Files.notExists(filePath)) {
      throw new NoSuchElementException("File with key " + binaryContentId + " does not exist");
    }
    try {
      return Files.newInputStream(filePath);
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  private Path resolvePath(UUID key) {
    return root.resolve(key.toString());
  }

  //파일의 메타 데이터와 바이너리 데이터를 조합해 바로 다운로드할 수 있는 HTTP 응답으로 변환
  @Override
  public ResponseEntity<Resource> download(BinaryContentDto metaData) {
    InputStream inputStream = get(metaData.id());
    Resource resource = new InputStreamResource(inputStream);

    return ResponseEntity
        .status(HttpStatus.OK)
        .header(HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"" + metaData.fileName() + "\"")
        .header(HttpHeaders.CONTENT_TYPE, metaData.contentType())
        .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(metaData.size()))
        .body(resource);
  }
}
