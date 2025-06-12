package com.sprint.mission.discodeit.async;

import com.sprint.mission.discodeit.entity.BinaryContentUploadStatus;
import com.sprint.mission.discodeit.entity.NotificationType;
import com.sprint.mission.discodeit.exception.file.FileSaveFailedException;
import com.sprint.mission.discodeit.notification.NotificationEvent;
import com.sprint.mission.discodeit.notification.NotificationEventPublisher;
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
    private final NotificationEventPublisher notificationEventPublisher;

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

        AsyncTaskFailure failure = new AsyncTaskFailure("BinaryUpload", requestId, e.getMessage());
        log.error("업로드 실패: {}", failure);

        try {
            UUID userId = UUID.fromString(requestId); //userId 추출

            notificationEventPublisher.publish(new NotificationEvent(
                    userId,
                    "파일 업로드 실패",
                    "파일 업로드 중 문제가 발생했어요.",
                    NotificationType.ASYNC_FAILED,
                    null
            ));
        } catch (IllegalArgumentException ex) {
            log.warn("requestId를 userId로 파싱하지 못했습니다: {}", requestId, ex);
        }
    }
}