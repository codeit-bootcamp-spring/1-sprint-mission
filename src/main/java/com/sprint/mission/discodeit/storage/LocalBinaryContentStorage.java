package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.entity.AsyncTaskFailure;
import com.sprint.mission.discodeit.entity.CustomUserDetails;
import com.sprint.mission.discodeit.entity.NotificationType;
import com.sprint.mission.discodeit.event.AsyncTaskFailureEvent;
import com.sprint.mission.discodeit.execption.ErrorCode;
import com.sprint.mission.discodeit.repository.jpa.AsyncTaskFailureRepository;
import jakarta.annotation.PreDestroy;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.slf4j.MDC;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "local")
public class LocalBinaryContentStorage implements BinaryContentStorage {

    private final Path root;
    private final AsyncTaskFailureRepository asyncTaskFailureRepository;
    private final ApplicationEventPublisher publisher;

    public LocalBinaryContentStorage(Path root,
        AsyncTaskFailureRepository asyncTaskFailureRepository,
        ApplicationEventPublisher publisher) {
        this.root = root;
        this.asyncTaskFailureRepository = asyncTaskFailureRepository;
        this.publisher = publisher;
        init();
    }

    private void init() {
        try {
            if (!Files.exists(root)) {
                Files.createDirectories(root);
            }
        } catch (IOException e) {
            throw new RuntimeException(ErrorCode.DIRECTORY_INIT_FAIL.format("path: " + root));
        }
    }

    @Async
    @Retryable(
        retryFor = {RuntimeException.class},
        maxAttempts = 2,
        backoff = @Backoff
    )
    @Override
    public CompletableFuture<UUID> putAsync(UUID id, byte[] content) throws InterruptedException {
        MDC.put("taskName", "putBinaryContent");  // 로그 및 추적용

        save(resolvePath(id), content);
        Thread.sleep(10000);
        return CompletableFuture.completedFuture(id);
    }

    public void put(UUID id, byte[] content) throws InterruptedException {
        save(resolvePath(id), content);
        Thread.sleep(10000);
    }

    @Recover
    public CompletableFuture<Void> recover(RuntimeException e, UUID id, byte[] content) {
        String taskName = MDC.get("taskName");
        String requestId = MDC.get("requestId");

        AsyncTaskFailure failure = new AsyncTaskFailure(taskName, requestId, e.getMessage());
        asyncTaskFailureRepository.save(failure);

        SecurityContext securityContext = SecurityContextHolder.getContext();
        CustomUserDetails userDetails = (CustomUserDetails) securityContext.getAuthentication()
            .getPrincipal();

        publisher.publishEvent(new AsyncTaskFailureEvent(NotificationType.ASYNC_FAILED, taskName,
            userDetails.getUsername()));

        return CompletableFuture.failedFuture(e);
    }


    @Override
    public InputStream get(UUID id) {
        return load(root, id);
    }

    @Override
    public ResponseEntity<Resource> download(BinaryContentDto binaryContentDto) {
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + binaryContentDto.fileName() + "\"")
            .body(new InputStreamResource(get(binaryContentDto.id())));
    }

    private Path resolvePath(UUID id) {
        return root.resolve(id.toString());
    }

    private <T> void save(Path filePath, T data) {
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(filePath))) {
            oos.writeObject(data);
        } catch (IOException e) {
            throw new RuntimeException(ErrorCode.FILE_WRITE_FAIL
                .format("path: " + filePath + "data: " + data));
        }
    }

    public InputStream load(Path directory, UUID id) {
        try {
            if (!Files.exists(directory)) {
                return null;
            }
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory)) {
                for (Path path : stream) {
                    try {
                        File file = path.toFile();
                        String fileName = file.getName();
                        if (!fileName.equals(id.toString())) {
                            continue;
                        }
                        return new FileInputStream(file);
                    } catch (IOException e) {
                        throw new RuntimeException(
                            ErrorCode.FILE_READ_FAIL.format("path: " + path.toString()));
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(ErrorCode.FILES_LOAD_FAIL.format("path: " + directory));
        }
        return null;
    }

    @PreDestroy
    public void clearDataDirectory() {
        try {
            if (Files.exists(root)) {
                Files.walk(root)
                    .sorted((a, b) -> b.compareTo(a)) // 파일부터 삭제 후 디렉토리 삭제
                    .forEach(path -> {
                        try {
                            Files.deleteIfExists(path);
                        } catch (IOException e) {
                            throw new RuntimeException(ErrorCode.FILE_REMOVE_FAIL
                                .format("path: " + path.toString()));
                        }
                    });
            }
        } catch (IOException e) {
            throw new RuntimeException(ErrorCode.FILES_LOAD_FAIL.format("path: " + root));
        }
    }
}
