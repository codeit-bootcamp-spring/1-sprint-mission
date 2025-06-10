package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.binary.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContentUploadStatus;
import com.sprint.mission.discodeit.exception.file.FileUploadFailedException;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import org.springframework.http.ResponseEntity;

public interface BinaryContentStorage {

    CompletableFuture<Void> putAsync(UUID id, byte[] data, UUID userId,
            Consumer<BinaryContentUploadStatus> statusCallback);

    CompletableFuture<Void> recover(FileUploadFailedException e, UUID id, byte[] data, UUID userId,
            Consumer<BinaryContentUploadStatus> statusCallback);

    UUID put(UUID id, byte[] data) throws IOException;

    InputStream get(UUID id);

    ResponseEntity<?> download(BinaryContentDto binaryContentDto);
}
