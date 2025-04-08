package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.UUID;

import com.sprint.mission.discodeit.exception.binaryContent.BinaryContentGetFailedException;
import com.sprint.mission.discodeit.exception.binaryContent.BinaryContentSaveFailedException;
import com.sprint.mission.discodeit.exception.binaryContent.DirectoryInitFailedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "local")
public class LocalBinaryContentStorage implements BinaryContentStorage {

  private final Path root;

  // 루트 디렉토리 생성
  public LocalBinaryContentStorage(@Value("${discodeit.storage.local.root-path}") String rootPath) {
    this.root = Path.of(rootPath); // rootPath는 String이니까 받고 Path.of()로 Path로 변환
    init();
  }

  // 디렉토리 초기화 : 지정한 루트 디렉토리가 없을 때 디렉토리를 만들어줌
  private void init() {
    try {
      Files.createDirectories(root);
    } catch (IOException e) {
      throw new DirectoryInitFailedException(null);
    }
  }

  // 바이너리 데이터 저장 : 아이디가 파일명
  // TODO 대용량 파일 시 스트림으로 리팩토링
  @Override
  public UUID put(UUID contentId, byte[] data) {
      Path filePath = resolvePath(contentId);
      try {
          Files.write(filePath, data, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
      } catch (IOException e) {
          throw new BinaryContentSaveFailedException(null);
      }
      return contentId;
    }

  // 파일을 inputstream으로 반환
  @Override
  public InputStream get(UUID contentId) {
    try {
      return Files.newInputStream(resolvePath(contentId));
    } catch (IOException e) {
      throw new BinaryContentGetFailedException(null);
    }
  }

  // 파일 다운로드
  @Override
  public ResponseEntity<Resource> download(BinaryContentDto dto) {
    Path filePath = resolvePath(dto.getId());

    // 파일 존재 여부 체크
    if (!Files.exists(filePath)) {
      return ResponseEntity.notFound().build(); // HTTP 404 응답
    }

    Resource fileResource = new FileSystemResource(filePath);
    return ResponseEntity.ok().body(fileResource);
  }

  // 파일의 실제 저장 위치에 대한 규칙을 정의
  // 현재 root: ${user.dir}/storage 여기서 root.resolve(id.toString())을 하면 -> 경로 ex: ${user.dir}/storage/123e4567-e89b-12d3-a456-426614174000
  private Path resolvePath(UUID id) {
    return root.resolve(
        id.toString()); // (path는 Path 객체) path.resolve("추가경로") : 상대 경로를 덧붙여 새로운 Path 생성 -> 결과적으로 id가 파일명이 됨
  }
}


