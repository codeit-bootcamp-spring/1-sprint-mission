package com.sprint.mission.discodeit.storage;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.BinaryContentUploadStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@EnableAsync
@ActiveProfiles("test")
public class BinaryContentStorageAsyncTest {

    @Autowired
    private BinaryContentStorage storage;

    @Autowired
    private BinaryContentRepository repository;

    @Test
    void 파일업로드_비동기_성공() throws ExecutionException, InterruptedException {
        byte[] dummy = "hello".getBytes();

        BinaryContent binaryContent = new BinaryContent("test.txt", dummy.length, "text/plain");
        binaryContent.setUploadStatus(BinaryContentUploadStatus.WAITING);
        repository.save(binaryContent);

        CompletableFuture<Void> future = storage.putAsync(
                binaryContent.getId(),
                dummy,
                status -> {
                    BinaryContent updated = repository.findById(binaryContent.getId())
                            .orElseThrow();
                    updated.setUploadStatus(status);
                    repository.save(updated);
                }
        );

        future.get();
        BinaryContent result = repository.findById(binaryContent.getId()).orElseThrow();
        assertEquals(BinaryContentUploadStatus.SUCCESS, result.getUploadStatus());
    }

    @Test
    void 파일업도르_비동기_실패() throws InterruptedException {
        byte[] dummy = "fail".getBytes();

        BinaryContent binaryContent = new BinaryContent("test.txt", dummy.length, "text/plain");
        binaryContent.setUploadStatus(BinaryContentUploadStatus.WAITING);
        repository.save(binaryContent);

        CompletableFuture<Void> future = storage.putAsync(
                binaryContent.getId(),
                dummy,
                status -> {
                    BinaryContent updated = repository.findById(binaryContent.getId())
                            .orElseThrow();
                    updated.setUploadStatus(status);
                    repository.save(updated);
                }
        );

        try {
            future.get();
        } catch (Exception ignored) {
        }

        Thread.sleep(500);

        await().atMost(2, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    BinaryContent result = repository.findById(binaryContent.getId()).orElseThrow();
                    assertEquals(BinaryContentUploadStatus.FAILED, result.getUploadStatus());
                });
    }
}
