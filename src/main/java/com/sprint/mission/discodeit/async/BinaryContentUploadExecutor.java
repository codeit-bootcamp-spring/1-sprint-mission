package com.sprint.mission.discodeit.async;

import com.sprint.mission.discodeit.entity.BinaryContentUploadStatus;
import com.sprint.mission.discodeit.exception.file.FileSaveFailedException;
import com.sprint.mission.discodeit.repository.jpa.BinaryContentRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jboss.logging.MDC;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class BinaryContentUploadExecutor {

    private final BinaryContentStorage storage;
    private final BinaryContentRepository binaryContentRepository;

    @Async
    @Retryable(
            value = FileSaveFailedException.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000)
    )
    public void uploadAsync(UUID id, byte[] bytes, String requestId) {
        try {
            MDC.put("requestId", requestId);
            storage.put(id, bytes);
            binaryContentRepository.updateUploadStatus(id, BinaryContentUploadStatus.SUCCESS);
            log.info("업로드 성공 - id: {}", id);
        } finally {
            MDC.clear();
        }
    }

    @Recover
    public void onUploadFailure(FileSaveFailedException e, UUID id, byte[] bytes, String requestId) {
        binaryContentRepository.updateUploadStatus(id, BinaryContentUploadStatus.FAILED);

        // 로그로 실패 정보 기록
        AsyncTaskFailure failure = new AsyncTaskFailure("BinaryUpload", requestId, e.getMessage());
        log.error("업로드 실패: {}", failure);
    }
}