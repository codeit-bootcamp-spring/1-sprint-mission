package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.BinaryContentDto;
import java.io.InputStream;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

public interface BinaryContentStorage {

  CompletableFuture<UUID> put(UUID id, byte[] data);

  InputStream get(UUID id);

  ResponseEntity<Resource> download(BinaryContentDto response);

  void delete(UUID id);
}
