package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.binary.BinaryContentDto;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.UUID;
import org.springframework.http.ResponseEntity;

public interface BinaryContentStorage {

  Path put(UUID id, byte[] data, String extension);

  InputStream get(UUID id, String extension);

  ResponseEntity<?> download(BinaryContentDto binaryContentDto, String extension);

  boolean exists(UUID id, String extension);

  void delete(UUID id, String extension);
}
