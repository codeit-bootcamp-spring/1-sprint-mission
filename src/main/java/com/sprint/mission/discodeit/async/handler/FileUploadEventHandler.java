package com.sprint.mission.discodeit.async.handler;

import com.sprint.mission.discodeit.async.event.AsyncFailedNotificationEvent;
import com.sprint.mission.discodeit.async.event.FileUploadEvent;
import com.sprint.mission.discodeit.async.failure.AsyncTaskFailure;
import com.sprint.mission.discodeit.async.failure.AsyncTaskFailureRepository;
import com.sprint.mission.discodeit.common.NotificationType;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.BinaryContent.BinaryContentUploadStatus;
import com.sprint.mission.discodeit.exception.binarycontent.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import jakarta.persistence.EntityManager;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileUploadEventHandler {

  private static final String REQUEST_ID = "requestId";
  private static final String TASK_NAME = "FileUploadEvent";

  private final BinaryContentStorage binaryContentStorage;
  private final AsyncTaskFailureRepository asyncTaskFailureRepository;
  private final BinaryContentRepository binaryContentRepository;

  private final FileUploadEventHandler self = this;
  private final EntityManager entityManager;
  private final TransactionTemplate transactionTemplate;
  private final ApplicationEventPublisher applicationEventPublisher;

  @Async("unifiedPool")
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handle(FileUploadEvent event) {
    try {
      self.uploadWithRetry(event.getFileId(), event.getFile());
      // 상태 업데이트
      binaryContentRepository.findById(event.getFileId()).ifPresent(content -> {
        content.updateUploadStatus(BinaryContentUploadStatus.SUCCESS);
        binaryContentRepository.saveAndFlush(content);
      });
    } catch (Exception e) {
      self.recover(e, event);
    }
  }

  @Retryable(
      value = {Exception.class},
      maxAttempts = 3,
      backoff = @Backoff(delay = 1000, multiplier = 2)
  )
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void uploadWithRetry(UUID fileId, MultipartFile file) {
    log.info("파일 저장 시도 - fileId: {}", fileId);
    binaryContentStorage.put(fileId, file).join();
  }

  @Recover
  @Transactional(propagation = Propagation.REQUIRES_NEW)  // 신규 트랜잭션 강제
  public void recover(Exception e, FileUploadEvent event) {
    log.error("파일 저장 재시도 실패 - fileId: {}, filename: {}", event.getFileId(),
        event.getFile().getOriginalFilename());
    entityManager.clear();

    BinaryContent content = binaryContentRepository.findById(event.getFileId())
        .orElseThrow(() -> new BinaryContentNotFoundException(event.getFileId()));

    content.updateUploadStatus(BinaryContentUploadStatus.FAILED);
    binaryContentRepository.saveAndFlush(content);
    binaryContentStorage.delete(event.getFileId());

    String requestIdStr = MDC.get(REQUEST_ID);
    UUID requestId = requestIdStr != null ? UUID.fromString(requestIdStr) : null;

    transactionTemplate.executeWithoutResult(transactionStatus -> {
          AsyncTaskFailure failure = AsyncTaskFailure.builder()
              .requestId(requestIdStr)
              .taskName(TASK_NAME)
              .failureReason(e.getMessage())
              .build();
          asyncTaskFailureRepository.save(failure);
          applicationEventPublisher.publishEvent(
              new AsyncFailedNotificationEvent(event.getReceiverId(), requestId,
                  NotificationType.ASYNC_FAILED));
        }
    );
  }
}
