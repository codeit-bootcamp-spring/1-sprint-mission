package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.async.AsyncTaskFailure;
import com.sprint.mission.discodeit.dto.binary.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContentUploadStatus;
import com.sprint.mission.discodeit.entity.NotificationType;
import com.sprint.mission.discodeit.event.NotificationEvent;
import com.sprint.mission.discodeit.exception.file.FileUploadFailedException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;

@Slf4j
@Conditional(LocalStorageCondition.class)
@RequiredArgsConstructor
public class LocalBinaryContentStorage implements BinaryContentStorage {

    private final Path root;
    private final ApplicationEventPublisher eventPublisher;

    public LocalBinaryContentStorage(
            @Value("${discodeit.storage.local.root-path}") String rootPath,
            ApplicationEventPublisher eventPublisher) {
        this.root = Paths.get(rootPath);
        this.eventPublisher = eventPublisher;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize local binary content storage", e);
        }
    }

    @Async("contextAwareExecutor")
    @Retryable(
            value = {FileUploadFailedException.class},
            maxAttempts = 3, //3번까지 시도
            backoff = @Backoff(delay = 1000)
    )
    public CompletableFuture<Void> putAsync(UUID id, byte[] data,
            UUID userId,
            Consumer<BinaryContentUploadStatus> statusCallback) {
        try {
            put(id, data);
            statusCallback.accept(BinaryContentUploadStatus.SUCCESS);
            return CompletableFuture.completedFuture(null);
        } catch (FileUploadFailedException | IOException e) {
            log.error("비동기 파일 업로드 실패: {}", id);
            eventPublisher.publishEvent(NotificationEvent.of(
                    List.of(userId),
                    "파일 업로드 실패",
                    "첨부파일 업로드 중 문제가 발생했습니다.",
                    NotificationType.ASYNC_FAILED,
                    null
            ));
            throw new FileUploadFailedException();
        }
    }

    @Recover
    public CompletableFuture<Void> recover(FileUploadFailedException e, UUID id, byte[] data,
            UUID userId,
            Consumer<BinaryContentUploadStatus> statusCallback) {
        String requestId = MDC.get("requestId");
        String taskName = "FileUpload";
        String reason = e.getMessage();

        AsyncTaskFailure failure = new AsyncTaskFailure(taskName, requestId, reason);
        log.error("[UPLOAD RECOVERY] 비동기 작업 실패 - {}", failure);
        statusCallback.accept(BinaryContentUploadStatus.FAILED);
        return CompletableFuture.failedFuture(e);
    }

    @Override
    public UUID put(UUID id, byte[] data) throws IOException {
        String fileName = id.toString();

        Path filePath = resolvePath(fileName);

        if (new String(data).equals("fail")) {
            throw new IOException("강제 실패");
        }
        try {
            Thread.sleep(1000);
            Files.write(filePath, data);
            log.info("[UPLOAD SUCCESS] 저장 성공 -{}", id);
            return id;
        } catch (IOException e) {
            log.warn("[UPLOAD ERROR] 저장 실패 - {}", id, e);
            throw new FileUploadFailedException();
        } catch (InterruptedException e) {
            throw new IOException("업로드중 인터럽트 발생", e);
        }
    }

    @Override
    public InputStream get(UUID id) {
        Path filePath = resolvePath(id.toString());
        try {
            return Files.newInputStream(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file: " + filePath, e);
        }
    }

    @Override
    public ResponseEntity<?> download(BinaryContentDto binaryContentDto) {
        InputStream inputStream = get(binaryContentDto.getId());
        Resource resource = new InputStreamResource(inputStream);
        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + binaryContentDto.getFileName() + "\"")
                .header(HttpHeaders.CONTENT_TYPE, binaryContentDto.getContentType())
                .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(binaryContentDto.getSize()))
                .body(resource);
    }

    private Path resolvePath(String fileName) {
        return root.resolve(fileName);
    }
}
