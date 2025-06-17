package com.sprint.mission.discodeit.storage.local;

import com.sprint.mission.discodeit.entity.AsyncTaskFailure;
import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.entity.NotificationType;
import com.sprint.mission.discodeit.entity.UploadStatus;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.repository.AsyncTaskFailureRepository;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDateTime;
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
@Service
@RequiredArgsConstructor
public class AsyncBinaryContentWriter {

  private final AsyncTaskFailureRepository failureRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final NotificationRepository notificationRepository;
  private final UserRepository userRepository;

  @Async("binaryContentExecutor")
  @Retryable(
      value = IOException.class,
      maxAttempts = 3,
      backoff = @Backoff(delay = 1000, multiplier = 2)
  )
  public void write(UUID binaryContentId, byte[] bytes, Path path) throws IOException {
    try (OutputStream outputStream = Files.newOutputStream(path)) {
      outputStream.write(bytes);
      binaryContentRepository.findById(binaryContentId).ifPresent(entity -> {
        entity.setUploadStatus(UploadStatus.SUCCESS);
        binaryContentRepository.save(entity);
      });
      log.info("비동기 저장 성공: id={}, size={}", binaryContentId, bytes.length);
    } catch (IOException e) {
      log.warn("비동기 저장 실패 (재시도 대상): id={}, message={}", binaryContentId, e.getMessage());
      throw e; // 반드시 예외를 던져야 재시도 작동
    }
  }

  @Recover
  public void recover(IOException e,  UUID userId, UUID binaryContentId, byte[] bytes, Path path) {
    log.error("비동기 파일 업로드 실패: userId={}, error={}", userId, e.getMessage());

    String requestId = MDC.get("requestId");
    AsyncTaskFailure failure = AsyncTaskFailure.builder()
        .taskName("binary-content-upload")
        .requestId(requestId != null ? requestId : "unknown")
        .failureReason(e.getMessage())
        .failedAt(LocalDateTime.now())
        .build();

    binaryContentRepository.findById(binaryContentId).ifPresent(entity -> {
      entity.setUploadStatus(UploadStatus.FAILED);
      binaryContentRepository.save(entity);
    });
    failureRepository.save(failure);

    Notification notification = new Notification(
        userRepository.findById(userId).orElseThrow(UserNotFoundException::new),
        "비동기 작업 실패",
        "요청한 작업이 실패했습니다. 다시 시도해주세요.",
        NotificationType.ASYNC_FAILED,
        null, // targetId 없음
        Instant.now()
    );
    notificationRepository.save(notification);
  }
}
