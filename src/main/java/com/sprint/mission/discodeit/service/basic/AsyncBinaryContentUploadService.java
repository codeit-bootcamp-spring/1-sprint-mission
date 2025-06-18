package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.AsyncTaskFailure;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.BinaryContent.uploadStatus;
import com.sprint.mission.discodeit.entity.Notification.NotificationType;
import com.sprint.mission.discodeit.exception.binaryContent.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.repository.AsyncTaskFailureRepository;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.NotificationService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AsyncBinaryContentUploadService {

  private final BinaryContentStorage storage;
  private final BinaryContentRepository repository;
  private final AsyncTaskFailureRepository failureRepository;
  private final NotificationService notificationService;

  @Async
  @Retryable(
      value = {IOException.class},
      maxAttempts = 3,
      backoff = @Backoff(delay = 2000)
  )
  public void uploadAsync(UUID contentId, byte[] bytes) throws IOException {
    log.debug("비동기 업로드 시작: id={}", contentId);

    storage.put(contentId, bytes);

    BinaryContent content = repository.findById(contentId)
        .orElseThrow(() -> BinaryContentNotFoundException.withId(contentId));

    content.updateStatus(uploadStatus.SUCCESS);
    repository.save(content);

    log.info("업로드 성공: id={}", contentId);
  }

  @Recover
  public void onUploadFailure(IOException e, UUID contentId) {
    log.error("업로드 실패 (재시도 모두 실패): id={}, message={}", contentId, e.getMessage());

    String requestId = MDC.get("requestId");

    failureRepository.save(new AsyncTaskFailure(
        "uploadAsync",
        requestId,
        e.getMessage()
    ));

    repository.findById(contentId).ifPresent(content -> {
      content.updateStatus(uploadStatus.FAILED);
      repository.save(content);
    });

    String content = String.format("파일 업로드 실패 - 파일 ID: %s, 요청 ID: %s", contentId, requestId);
    notificationService.createNotification(content, NotificationType.ASYNC_FAILED, null);
  }
}
