package com.sprint.mission.discodeit.storage.local;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import io.micrometer.core.annotation.Timed;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "local")
@Component
public class LocalBinaryContentStorage implements BinaryContentStorage {

    private final Path root;

    public LocalBinaryContentStorage(
        @Value("${discodeit.storage.local.root-path}") Path root
    ) {
        this.root = root;
    }

    @PostConstruct
    public void init() {
        if (!Files.exists(root)) {
            try {
                Files.createDirectories(root);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

    @Timed(value = "binary.content.upload.sync", description = "Time taken for synchronous file uplaod")
    public UUID put(UUID binaryContentId, byte[] bytes) {
        Path filePath = resolvePath(binaryContentId);
        if (Files.exists(filePath)) {
            throw new IllegalArgumentException(
                "File with key " + binaryContentId + " already exists");
        }

        //성능 비교를 위한 지연
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        try (OutputStream outputStream = Files.newOutputStream(filePath)) {
            outputStream.write(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return binaryContentId;
    }

    @Async("fileUploadExecutor")
    @Timed(value = "binary.content.upload.async", description = "Time taken for asynchronous file upload")
    @Retryable(
        retryFor = {Exception.class},
        maxAttempts = 3,
        backoff = @Backoff(delay = 1000, multiplier = 2.0)
    )
    public CompletableFuture<UUID> putAsync(UUID binaryContentId, byte[] bytes) {
        log.info("비동기 파일 업로드 시작: id={}", binaryContentId);

        Path filePath = resolvePath(binaryContentId);
        if (Files.exists(filePath)) {
            throw new IllegalArgumentException(
                "File with key " + binaryContentId + "already exists");
        }

        //성능 비교를 위한 지연
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        try (OutputStream outputStream = Files.newOutputStream(filePath)) {
            outputStream.write(bytes);
            log.info("비동기 파일 업로드 성공: id={}", binaryContentId);
            return CompletableFuture.completedFuture(binaryContentId);
        } catch (IOException e) {
            log.error("비동기 파일 업로드 실패: id={}", binaryContentId, e);
            throw new RuntimeException("파일 업로드 실패", e);
        }
    }

    @Recover
    public CompletableFuture<UUID> recoverFromUploadFailure(Exception ex, UUID binaryContentId,
        byte[] bytes) {
        log.error("모든 재시도 실패, 복구 로직 실행: id={}", binaryContentId, ex);
        return CompletableFuture.failedFuture(ex);
    }

    public InputStream get(UUID binaryContentId) {
        Path filePath = resolvePath(binaryContentId);
        if (Files.notExists(filePath)) {
            throw new NoSuchElementException(
                "File with key " + binaryContentId + " does not exist");
        }
        try {
            return Files.newInputStream(filePath);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private Path resolvePath(UUID key) {
        return root.resolve(key.toString());
    }

    @Override
    public ResponseEntity<Resource> download(BinaryContentDto metaData) {
        InputStream inputStream = get(metaData.id());
        Resource resource = new InputStreamResource(inputStream);

        return ResponseEntity
            .status(HttpStatus.OK)
            .header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + metaData.fileName() + "\"")
            .header(HttpHeaders.CONTENT_TYPE, metaData.contentType())
            .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(metaData.size()))
            .body(resource);
    }
}
