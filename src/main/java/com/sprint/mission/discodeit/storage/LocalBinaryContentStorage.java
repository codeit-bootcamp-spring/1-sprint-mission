package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "local")
public class LocalBinaryContentStorage implements BinaryContentStorage {
  private final Path root;

  public LocalBinaryContentStorage(@Value("${discodeit.storage.local.root-path}") String root) {
    this.root = Paths.get(root);
  }

  //디렉토리 초기화
  @PostConstruct
  public void init(){
    try{
      Files.createDirectories(root);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
  private Path resolvePath(UUID id){
    return root.resolve(id.toString());
  }

  @Override
  public UUID put(UUID id, byte[] bytes) {
    Path path = resolvePath(id);
    try{
      Files.write(path, bytes);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return id;
  }

  @Override
  public InputStream get(UUID id) {
    Path path = resolvePath(id);
    try{
      return Files.newInputStream(path);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public ResponseEntity<?> download(BinaryContentDto binaryContentDto) {
    InputStream inputStream = get(binaryContentDto.id());
    InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
    return ResponseEntity.ok(inputStreamResource);
  }

}
