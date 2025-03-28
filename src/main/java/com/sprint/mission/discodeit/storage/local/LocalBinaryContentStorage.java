package com.sprint.mission.discodeit.storage.local;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentDto;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "local")
// discodeit.storage.type이 local일 때만 컴포넌트 활성화
// -> application.yaml에서 discodeit.storage.type이 local로 설정되어 있을때만 사용됨
public class LocalBinaryContentStorage implements BinaryContentStorage {

  private final Path root;

  // 생성자
  public LocalBinaryContentStorage(
      @Value(".discodeit/storage") Path root
  ) {
    this.root = root;
  }

  @PostConstruct  // 객체가 초기화된 후 실행되는 메서드
  public void init() {
    if (!Files.exists(root)) {  // 디렉토리 존재하지 않을 경우
      try {
        Files.createDirectories(root);  // 디렉토리 생성
      } catch (IOException e) {
        e.printStackTrace();
        throw new RuntimeException(e);
      }
    }
  }

  // 파일 저장
  @Override
  public UUID put(UUID binaryContentId, byte[] bytes) {
    Path filePath = resolvePath(binaryContentId); // 파일 경로 계산
    if (Files.exists(filePath)) {   // 이미 파일 존재할 경우 예외 발생
      throw new IllegalArgumentException("File with key " + binaryContentId + " already exists");
    }
    try (OutputStream outputStream = Files.newOutputStream(filePath)) {   // 파일 생성
      outputStream.write(bytes);    // 파일에 데이터 저장
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return binaryContentId;
  }

  // 파일 읽기
  @Override
  public InputStream get(UUID binaryContentId) {
    Path filePath = resolvePath(binaryContentId);
    if (Files.notExists(filePath)) {  // 파일이 존재하지 않는 경우 예외 발생
      throw new NoSuchElementException("File with key " + binaryContentId + " does not exist");
    }
    try {
      return Files.newInputStream(filePath);  // 파일의 InputStream을 반환하여 읽을 수 있게 됨
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  // 파일 다운로드
  @Override
  public ResponseEntity<?> download(BinaryContentDto metaData) {
    InputStream inputStream = get(metaData.id());   // 파일 찾음

    // InputStream을 Resource로 변환하여 HTTP 응답 본문에 담을 수 있게 함
    InputStreamResource resource = new InputStreamResource(inputStream);

    // HTTP 응답 설정
    return ResponseEntity
        .status(HttpStatus.OK)
        .header(HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"" + metaData.fileName() + "\"") // 파일 다운로드 헤더 설정
        .header(HttpHeaders.CONTENT_TYPE, metaData.contentType()) // 파일의 MIME 타입
        .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(metaData.size()))  // 파일 크기 (바이트 단위)
        .body(resource);
  }


  // 파일 경로 계산
  private Path resolvePath(UUID key) {
    return root.resolve(key.toString());  // UUID로 받은 키를 root 경로에 연결하여 파일의 실제 경로 계산
  }
}