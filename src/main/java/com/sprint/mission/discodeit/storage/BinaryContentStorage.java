package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import java.io.InputStream;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.springframework.http.ResponseEntity;

public interface BinaryContentStorage {

  UUID put(UUID id, byte[] bytes);

  CompletableFuture<UUID> asyncPut(UUID id, byte[] bytes);

  InputStream get(UUID uuid);

  ResponseEntity<?> download(BinaryContentDto binaryContentDto);
}
