package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.BinaryContentResponse;
import com.sprint.mission.discodeit.global.exception.ErrorCode;
import com.sprint.mission.discodeit.global.exception.RestApiException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(value = "discodeit.storage.type", havingValue = "local", matchIfMissing = false)
public class LocalBinaryContentStorage implements BinaryContentStorage {

  private final Path root;

  public LocalBinaryContentStorage(@Value("${discodeit.storage.local.root-path}") Path path) {
    this.root = path;
    init();
  }

  public void init() {
    if (!Files.exists(root)) {
      try {
        Files.createDirectories(root);
      } catch (IOException e) {
        throw new RestApiException(ErrorCode.INTERNAL_SERVER_ERROR,
            "Failed to initialize local Binary content storage");
      }
    }
  }

  @Override
  public UUID put(UUID id, byte[] bytes) {
    try (
        FileOutputStream fileOutputStream = new FileOutputStream(resolvePath(id).toFile());
    ) {
      fileOutputStream.write(bytes);
    } catch (IOException e) {
      throw new RestApiException(ErrorCode.INTERNAL_SERVER_ERROR, "Failed to save binary content.");
    }
    return id;
  }

  @Override
  public InputStream get(UUID id) {
    if (!Files.exists(root)) {
      throw new RestApiException(ErrorCode.INTERNAL_SERVER_ERROR,
          "Failed to read binary content: No binary content available to read.");
    }
    try {
      return new FileInputStream(resolvePath(id).toFile());
    } catch (FileNotFoundException e) {
      throw new RestApiException(ErrorCode.INTERNAL_SERVER_ERROR,
          "Failed to create stream: File not found.");
    }
  }

  @Override
  public ResponseEntity<Resource> download(BinaryContentResponse binaryContentResponse) {
    //참고: "When using InputStreamResource, the underlying stream is closed automatically after the response is written."
    InputStream inputStream = get(binaryContentResponse.id());
    InputStreamResource resource = new InputStreamResource(inputStream);

    return ResponseEntity.status(HttpStatus.OK)
        .header(
            HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\""
                + binaryContentResponse.fileName() + "\"")
        .contentType(MediaType.valueOf(binaryContentResponse.contentType()))
        .body(resource);
  }

  private Path resolvePath(UUID uuid) {
    return root.resolve(uuid.toString());
  }

}
