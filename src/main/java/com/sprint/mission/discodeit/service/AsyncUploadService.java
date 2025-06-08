package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.AsyncTaskFailure;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.BinaryContentUploadStatus;
import com.sprint.mission.discodeit.repository.AsyncTaskFailureRepository;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import jakarta.transaction.Transactional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AsyncUploadService {

    private final BinaryContentStorage binaryContentStorage;
    private final BinaryContentRepository binaryContentRepository;
    private final AsyncTaskFailureRepository asyncTaskFailureRepository;

    public void uploadFileAsync(UUID binaryContentId, byte[] bytes) {
        String requestId = MDC.get("requestId");
        SecurityContext context = SecurityContextHolder.getContext();

        CompletableFuture<UUID> future = binaryContentStorage.putAsync(binaryContentId, bytes);

        future.whenComplete((result, throwable) -> {
            if (requestId != null) {
                MDC.put("requestId", requestId);
            }
            SecurityContextHolder.setContext(context);

            try {
                if (throwable == null) {
                    updateUploadStatusToSuccess(binaryContentId);
                    log.info("파일 업로드 성공: id={}", binaryContentId);
                } else {
                    updateUploadStatusToFailed(binaryContentId);
                    recordFailure("FILE_UPLOAD", requestId, throwable.getMessage());
                    log.error("파일 업로드 실패: id={}", binaryContentId, throwable);
                }
            } finally {
                MDC.clear();
                SecurityContextHolder.clearContext();
            }
        });
    }

    public void updateUploadStatusToSuccess(UUID binaryContentId) {
        BinaryContent binaryContent = binaryContentRepository.findById(binaryContentId)
            .orElse(null);

        if (binaryContent != null) {
            binaryContent.updateUploadStatus(BinaryContentUploadStatus.SUCCESS);
            binaryContentRepository.save(binaryContent);
        }
    }

    public void updateUploadStatusToFailed(UUID binaryContentId) {
        BinaryContent binaryContent = binaryContentRepository.findById(binaryContentId)
            .orElse(null);

        if (binaryContent != null) {
            binaryContent.updateUploadStatus(BinaryContentUploadStatus.FAILED);
            binaryContentRepository.save(binaryContent);
        }
    }

    @Transactional
    public void recordFailure(String taskName, String requestId, String failureReason) {
        AsyncTaskFailure failure = new AsyncTaskFailure(taskName, requestId,
            failureReason);
        asyncTaskFailureRepository.save(failure);
    }
}
