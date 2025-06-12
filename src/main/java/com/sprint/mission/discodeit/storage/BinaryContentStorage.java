package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.response.BinaryContentResponse;
import java.io.InputStream;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.springframework.http.ResponseEntity;

public interface BinaryContentStorage {

    CompletableFuture<Void> put(UUID id, byte[] bytes);

    InputStream get(UUID id);

    ResponseEntity<?> download(BinaryContentResponse binaryContentResponse);
}
