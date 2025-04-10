package com.sprint.mission.discodeit.storage.local;

import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LocalBinaryContentStorage implements BinaryContentStorage {


  private Path root;

  @Value("${discodeit.storage.local.root-path}")
  private String rootPath;

  @PostConstruct // @PostConstruct 어노테이션을 사용하면 스프링이 빈 생성 후 초기화 작업을 위해 지정된 메서드를 자동으로 호출
  public void init() {
    this.root = Paths.get(rootPath); // rootPath 값을 Path로 변환

    // 루트 디렉토리 초기화 = 경로 확인, 존재 여부 확인
    if (!Files.exists(root)) {
      try {
        Files.createDirectories(root);
      } catch (IOException e) {
        throw new RuntimeException("저장소 초기화 실패", e);
      }
    }
  }

  @Override
  public UUID put(UUID id, byte[] bytes) {
    log.info("파일 업로드 시도");
    // 파일 저장 경로 지정
    Path filePath = resolvePath(id);
    File file = filePath.toFile(); // Path 객체 -> File 객체

    // 파일 저장 로직
    try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
      fileOutputStream.write(bytes);
      fileOutputStream.flush(); // flush() [스트림 강제 비우기] 모든 데이터를 디스크에 길고
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
    log.info("파일 업로드 시도 성공");
    // 저장한 파일 UUID 반환
    return id;
  }

  @Override
  public InputStream get(UUID id) {
    // 파일 가져올 경로 지정
    Path filePath = resolvePath(id);
    File file = filePath.toFile();

    // 파일 불러오기 로직
    try {
      FileInputStream fileInputStream = new FileInputStream(file);
      return fileInputStream;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public ResponseEntity<Resource> download(BinaryContentDto binaryContentDto) {
    InputStream inputStream = get(binaryContentDto.id());

    // InputStream -> Resource 변환
    // Resource 는 Spring 에서 파일 처리 할 때 쓰이는 인터페이스
    Resource resource = new InputStreamResource(inputStream);

    // ResponseEntity 로 반환 (원래 컨트롤러 단에서 하던건데 여기서 해도 될까요? 안 될 건 없겠지만 신경쓰이는 느낌)
    return ResponseEntity.ok()
        .contentType(MediaType.APPLICATION_OCTET_STREAM) // 파일 다운로드, 임의 바이너리 데이터 전송
        .header(HttpHeaders.CONTENT_DISPOSITION,
            // CONTENT_DISPOSITION는 body 타입 명시인데 + attachment랑 같이 쓰이면 다운로드 받으라고 지시
            "attachment; filename=\"" + binaryContentDto.fileName() + "\"")
        .body(resource);
  }

  private Path resolvePath(UUID id) {
    return root.resolve(id.toString());
  }
}
