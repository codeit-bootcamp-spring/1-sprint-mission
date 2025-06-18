package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.response.BinaryContentDto;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.springframework.http.ResponseEntity;

public interface BinaryContentStorage {
  
  CompletableFuture<UUID> put(UUID fileId, byte[] bytes);

  InputStream get(UUID fileId);

  ResponseEntity<?> download(BinaryContentDto file);

  default Path resolvePath(UUID id) {
    return null;
  }

//  AsyncTaskFailure recover(NotSavedBinaryContentException e, UUID fileId, byte[] bytes,
//      Path filePath);
}
