package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.binary.BinaryContentDto;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.UUID;
import org.springframework.http.ResponseEntity;

public interface BinaryContentStorage {

  UUID put(UUID id, byte[] data);

  InputStream get(UUID id);
  
  ResponseEntity<?> download(BinaryContentDto binaryContentDto);
}
