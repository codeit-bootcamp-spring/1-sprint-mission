package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.exception.file.FileNotFoundCustomException;
import com.sprint.mission.discodeit.exception.file.FileReadFailedException;
import com.sprint.mission.discodeit.exception.file.FileSaveFailedException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "local")
public class LocalBinaryContentStorage implements BinaryContentStorage {

  private final Path root;

  public LocalBinaryContentStorage(@Value("${discodeit.storage.local.root-path}") String rootPath)
      throws IOException {
    this.root = Paths.get(rootPath);
    init();
  }

  private void init() throws IOException {
    if (!Files.exists(root)) {
      Files.createDirectories(root);
    }
  }

  @Override
  public UUID put(UUID binaryContentId, byte[] bytes) {
    Path filePath = resolvePath(binaryContentId);
    try {
      Files.write(filePath, bytes, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
      log.info("파일 저장 완료 - id: {}, 경로: {}", binaryContentId, filePath);
      return binaryContentId;
    } catch (IOException e) {
      log.error("파일 저장 실패 - id: {}, 에러: {}", binaryContentId, e.toString());
      throw new FileSaveFailedException(e.toString()); //파일 저장 실패
    }
  }

  @Override
  public InputStream get(UUID binaryContentId) {
    Path filePath = resolvePath(binaryContentId);
    if (!Files.exists(filePath)) {
      log.warn("파일 없음 - id: {}", binaryContentId);
      throw new FileNotFoundCustomException(filePath.toString()); //파일 찾을 수 없음
    }
    try {
      log.debug("파일 읽기 시작 - id: {}, 경로: {}", binaryContentId, filePath);
      return Files.newInputStream(filePath);
    } catch (IOException e) {
      log.error("파일 읽기 실패 - id: {}, 에러: {}", binaryContentId, e.toString());
      throw new FileReadFailedException(e.toString()); //파일 읽는 중 오류
    }
  }


  @Override
  public ResponseEntity<Resource> download(BinaryContentDto binaryContentDto) {

    log.info("파일 다운로드 요청 - id: {}, 파일명: {}", binaryContentDto.getId(),
        binaryContentDto.getFileName());

    InputStream inputStream = get(binaryContentDto.getId());
    InputStreamResource resource = new InputStreamResource(inputStream);

    return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType(binaryContentDto.getContentType()))
        .contentLength(binaryContentDto.getSize())
        .header(HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"" + binaryContentDto.getFileName() + "\"")
        .body(resource);
  }

  //파일의 실제 저장 위치에 대한 규칙을 정의
  //파일 저장 위치 규칙 예시: {root}/{UUID}
  public Path resolvePath(UUID binaryContentId) {
    return root.resolve(binaryContentId.toString());
  }
}
