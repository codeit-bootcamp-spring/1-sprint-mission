package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;

import com.sprint.mission.discodeit.service.basic.BasicBinaryContentService;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class LocalBinaryContentStorage implements BinaryContentStorage {

  private String root="./storage";
  private BasicBinaryContentService binaryContentService;

  LocalBinaryContentStorage(@Lazy BasicBinaryContentService binaryContentService){
    this.binaryContentService=binaryContentService;

  }

  @Override
  @Transactional
  public UUID put(UUID id, byte[] bytes) {
    String imgPath = root + File.separator + id.toString() + ".jpg";
    File file = new File(imgPath);
    try {
      file.getParentFile().mkdirs();
      Files.write(Path.of(imgPath), bytes);
    } catch (IOException e) {
      throw new RuntimeException("이미지 저장 실패", e);
    }
    //System.out.println(binaryContentService.findEntity(id).toString());
    binaryContentService.findEntity(id).setPath(imgPath);
    return id;
  }

  @Override
  public InputStream get(UUID id) {

    String filePath = binaryContentService.find(id).path();

    try {
      return new FileInputStream(filePath);
    } catch (FileNotFoundException e) {
      throw new RuntimeException("파일을 찾을 수 없습니다: " + filePath, e);
    }
  }

  @Override
  public ResponseEntity<byte[]> download(BinaryContentDto binaryContentDto) {
    String filePath = binaryContentDto.path();

    File file = new File(filePath);
    if (!file.exists()) {
      throw new RuntimeException("파일을 찾을 수 없습니다: " + filePath);
    }

    try {
      byte[] fileBytes = Files.readAllBytes(file.toPath());
      return ResponseEntity.ok()
          .header("Content-Disposition", "attachment; filename=" + file.getName())
          .header("Content-Type", Files.probeContentType(file.toPath()))
          .body(fileBytes);
    } catch (IOException e) {
      throw new RuntimeException("파일을 읽는 중 오류 발생: " + filePath, e);
    }
  }

  @PostConstruct
  public void init() {
    File directory = new File(root);
    if (!directory.exists()) {
      directory.mkdirs(); // 루트 디렉토리 생성
    }
  }
  private Path  resolvePath(UUID id) {
    return Path.of(root, id.toString() + ".bin");
  }
}
