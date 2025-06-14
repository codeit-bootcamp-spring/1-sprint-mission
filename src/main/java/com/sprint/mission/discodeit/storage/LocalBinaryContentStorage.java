package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.response.BinaryContentResponse;
import com.sprint.mission.discodeit.global.exception.ErrorCode;
import com.sprint.mission.discodeit.global.exception.binarycontent.BinaryContentOperationException;
import com.sprint.mission.discodeit.global.monitoring.AsyncTaskFailure;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(value = "discodeit.storage.type", havingValue = "local", matchIfMissing = false)
public class LocalBinaryContentStorage implements BinaryContentStorage {

    private static final String TASK_NAME = "file-upload";
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
                throw new BinaryContentOperationException(ErrorCode.BINARY_STORAGE_INIT_FAILED);
            }
        }
    }

    @Async("fileUploadTaskExecutor")
    @Retryable(
        value = BinaryContentOperationException.class,
        maxAttempts = 3,
        backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    @Override
    public CompletableFuture<Void> put(UUID id, byte[] bytes) {
        try (
            FileOutputStream fileOutputStream = new FileOutputStream(resolvePath(id).toFile());
        ) {
            fileOutputStream.write(bytes);
            log.info("File write operation completed for ID: {}", id);
            CompletableFuture<Void> result = CompletableFuture.completedFuture(null);
            log.info("Returning completed future: {}", result);
            return result;
        } catch (IOException e) {
            log.warn("Upload failed for {}, will retry", id, e);
            throw new BinaryContentOperationException(ErrorCode.BINARY_SAVE_FAILED);
        }
    }

    @Override
    public InputStream get(UUID id) {
        if (!Files.exists(root)) {
            throw new BinaryContentOperationException(ErrorCode.BINARY_READ_FAILED);
        }
        try {
            return new FileInputStream(resolvePath(id).toFile());
        } catch (FileNotFoundException e) {
            throw new BinaryContentOperationException(ErrorCode.STREAM_CREATION_FAILED);
        }
    }

    @Override
    public ResponseEntity<Resource> download(BinaryContentResponse binaryContentResponse) {
        //참고: "When using InputStreamResource, the underlying stream is closed automatically after the response is written."
        InputStream inputStream = get(binaryContentResponse.id());
        InputStreamResource resource = new InputStreamResource(inputStream);

        log.info("Binary content download succeeded - id: {}", binaryContentResponse.id());
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

    @Recover
    public CompletableFuture<Void> recover(BinaryContentOperationException e, UUID id,
        byte[] bytes) {
        String requestId = MDC.get("requestId");
        String failureReason = e.getMessage();

        AsyncTaskFailure failure = new AsyncTaskFailure(TASK_NAME, requestId, failureReason);
        log.error("Async task failed : {}", failure);

        CompletableFuture<Void> failed = new CompletableFuture<>();
        failed.completeExceptionally(e);
        return failed;
    }

}
