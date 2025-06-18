package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentResponseDto;
import java.io.InputStream;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface BinaryContentStorage {

  CompletableFuture<UUID> put(UUID fileId, MultipartFile file);

  InputStream get(UUID fileId);

  ResponseEntity<?> download(BinaryContentResponseDto binaryContentResponseDto);

  void delete(UUID fileId);
}
