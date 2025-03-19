package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class LocalBinaryContentStorage implements BinaryContentStorage {

  Map<UUID, byte[]> map;
  String projectPath = System.getProperty("user.dir") +
      "\\src\\main\\resources\\static\\files";

  @Override
  public UUID put(UUID id, byte[] bytes) {
    String fileName = "/" + id.toString();
    File file = new File(projectPath, fileName);
    try {
      file.createNewFile();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return id;
  }

  @Override
  public InputStream get(UUID id) {
    String fileName = "/" + id.toString();
    File file = new File(projectPath + fileName);
    try {
      return new ByteArrayInputStream(Files.readAllBytes(file.toPath()));
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public ResponseEntity<?> download(BinaryContentDto binaryContentDto) {
    if (map.containsKey(binaryContentDto.getId())) {
      byte[] response = map.get(binaryContentDto.getId());

      String fileName = binaryContentDto.getFileName();
      String mimeType = "application/octet-stream";

      ByteArrayResource resource = new ByteArrayResource(response);

      HttpHeaders headers = new HttpHeaders();
      headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"");
      headers.add(HttpHeaders.CONTENT_TYPE, mimeType);

      return ResponseEntity.ok()
          .headers(headers)
          .contentLength(response.length)
          .body(resource);
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }

  @PostConstruct
  public void init() {

    File file = new File(projectPath);
    if (!file.exists()) {
      try {
        if (file.createNewFile()) {
          System.out.println("파일이 성공적으로 생성되었습니다.");
        } else {
          System.out.println("파일 생성에 실패했습니다.");
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    } else {
      try (FileWriter writer = new FileWriter(file)) {
        writer.write(""); // 파일 내용을 비웁니다.
        writer.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  Path resolvePath(UUID id) {
    return Paths.get(System.getProperty("user.dir") +
        "\\src\\main\\resources\\static\\files" + '/' + id);
  }

}
