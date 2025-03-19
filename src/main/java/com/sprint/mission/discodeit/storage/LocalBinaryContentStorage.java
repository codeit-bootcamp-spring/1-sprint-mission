package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "local")
public class LocalBinaryContentStorage implements BinaryContentStorage {

  private final Path root;

  public LocalBinaryContentStorage(@Value("${discodeit.storage.local.root-path}") String rootPath) {
    this.root = Path.of(rootPath); // @Value는 문자열 주입이니까 String으로 받고 Path.of()로 Path로 변환
    init();
  }

  // 파일 디렉토리 초기화 = 지정한 루트 디렉토리가 없을 때 디렉토리를 만들어줌
  private void init() {
    try {
      Files.createDirectories(root);
    } catch (IOException e) {
      throw new RuntimeException("파일 디렉토리 초기화에 실패했습니다.");
    }
  }

  // 아이디가 파일명
  @Override
  public UUID put(UUID contentId, byte[] data) {
    try {
      Path filePath = resolvePath(contentId); // Id별 추가 경로 설정
      Files.write(filePath, data, StandardOpenOption.CREATE,
          StandardOpenOption.TRUNCATE_EXISTING); // 그 경로에 wirte (파일 쓰기)
      return contentId;
    } catch (IOException e) {
      throw new RuntimeException("파일 저장에 실패했습니다: " + contentId, e);
    }
  }

  @Override
  public InputStream get(UUID contentId) {
    try {
      return Files.newInputStream(resolvePath(contentId));
    } catch (IOException e) {
      throw new RuntimeException("파일을 얻지 못했습니다: " + contentId, e);
    }
  }
  /* [ try문 안에 이렇게 쓸 수도 있음 java.io 기반 (구식) ]
      FileInputStream fis = new FileInputStream(new File(resolvePath(contentId)));
      return fis;
   */

  @Override
  public ResponseEntity<Resource> download(BinaryContentDto dto) {
    Path filePath = resolvePath(dto.id());

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

//@Component
//@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "local")
//public class LocalBinaryContentStorage implements BinaryContentStorage {
//
//  private final Path root;
//
//  public LocalBinaryContentStorage(@Value("${discodeit.storage.local.root-path}") Path rootPath) {
//    this.root = Path.of(rootPath);
//    init(); // 빈 생성과 동시에 호출 = 생성자에 넣으라는 말 -> 당연 생성자로 의존성 주입할 때 루트 디렉토리가 없을 수 있으니까 한 쌍으로 묶여야함
//  }
//
//  // 파일 디렉토리 초기화 = 지정한 루트 디렉토리가 없을 때 디렉토리를 만들어줌
//  private void init() {
//    try {
//      Files.createDirectories(root);
//    } catch (IOException e) {
//      throw new RuntimeException("파일 디렉토리 초기화에 실패했습니다.");
//    }
//  }
//
//  // 아이디가 파일명
//  @Override
//  public UUID put(UUID contentId, byte[] data) {
//    try {
//      Path filePath = resolvePath(contentId); // Id별 추가 경로 설정
//      Files.write(filePath, data, StandardOpenOption.CREATE,
//          StandardOpenOption.TRUNCATE_EXISTING); // 그 경로에 wirte (파일 쓰기)
//      return contentId;
//    } catch (IOException e) {
//      throw new RuntimeException("파일 저장에 실패했습니다: " + contentId, e);
//    }
//  }
//
//  @Override
//  public InputStream get(UUID contentId) {
//    try {
//      return Files.newInputStream(resolvePath(contentId));
//    } catch (IOException e) {
//      throw new RuntimeException("파일을 얻지 못했습니다: " + contentId, e);
//    }
//  }
//  /* [ try문 안에 이렇게 쓸 수도 있음 java.io 기반 (구식) ]
//      FileInputStream fis = new FileInputStream(new File(resolvePath(contentId)));
//      return fis;
//   */
//
//  // TODO 찐: Resource 클래스 ?
//  @Override
//  public ResponseEntity<Resource> download(BinaryContentDto dto) {
//    Path filePath = resolvePath(dto.id());
//
//    // 파일 존재 여부 체크
//    if (!Files.exists(filePath)) {
//      return ResponseEntity.notFound().build(); // HTTP 404 응답
//    }
//
//    Resource fileResource = new FileSystemResource(filePath);
//    return ResponseEntity.ok().body(fileResource);
//  }
//
//  // 파일의 실제 저장 위치에 대한 규칙을 정의
//  // 현재 root: ${user.dir}/storage 여기서 root.resolve(id.toString())을 하면 -> 경로 ex: ${user.dir}/storage/123e4567-e89b-12d3-a456-426614174000
//  private Path resolvePath(UUID id) {
//    return root.resolve(
//        id.toString()); // (path는 Path 객체) path.resolve("추가경로") : 상대 경로를 덧붙여 새로운 Path 생성 -> 결과적으로 id가 파일명이 됨
//  }
//}
//
