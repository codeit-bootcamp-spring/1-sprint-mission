package com.spirnt.mission.discodeit.storage;

import com.spirnt.mission.discodeit.dto.binaryContent.BinaryContentDto;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "local")
public class LocalBinaryContentStorage implements BinaryContentStorage {

  private Path root = Paths.get(System.getProperty("user.dir"), "localStorage");


  // Bean 생성 시 자동 호출하여 루트 디렉토리 초기화
  // 테스트를 위해 잠시 꺼둠
//  @PostConstruct
  void init() {
    File[] files = root.toFile().listFiles();
    if (files != null) {
      for (File file : files) {
        if (file.isFile()) {
          file.delete();
        }
      }
    }
  }

  /// 파일 실제 저장 위치 정의
  Path resolvePath(UUID binaryContentId) {
    return Paths.get(root + "/" + binaryContentId);
  }

  // 파일을 로컬에 저장
  @Override
  public UUID put(UUID binaryContentId, byte[] bytes) {
    Path path = resolvePath(binaryContentId);
    File file = new File(String.valueOf(path));
    try (FileOutputStream fos = new FileOutputStream(file)) {
      fos.write(bytes);
    } catch (IOException e) {
      return null;
    }
    return binaryContentId;
  }

  // 파일을 InputStream 데이터 타입으로 반환
  @Override
  public InputStream get(UUID binaryContentId) {
    Path path = resolvePath(binaryContentId);
    try {
      return new FileInputStream(String.valueOf(path));
    } catch (FileNotFoundException e) {
      throw new NoSuchElementException("File with: " + binaryContentId + " not found");
    }
  }

  // 파일 다운로드
  @Override
  public ResponseEntity<Resource> download(BinaryContentDto binaryContentDto) {
    Path path = resolvePath(binaryContentDto.getId());
    Resource resource = new FileSystemResource(path);
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_TYPE, binaryContentDto.getContentType())
        .header(HttpHeaders.CONTENT_DISPOSITION,
            "attachment;filename=\"" + binaryContentDto.getFileName() + "\"")
        .body(resource);
  }
}
