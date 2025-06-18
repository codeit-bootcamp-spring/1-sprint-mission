package com.sprint.mission.discodeit.async.binary_content;


import com.sprint.mission.discodeit.async.AsyncTaskFailure;
import com.sprint.mission.discodeit.async.AsyncTaskFailureRepository;
import com.sprint.mission.discodeit.entity.NotificationType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.event.event_entity.NotificationEvent;
import com.sprint.mission.discodeit.exception.file.FileException;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BinaryContentStorageAsyncService {

  private final BinaryContentStorage binaryContentStorage;
  private final AsyncTaskFailureRepository asyncTaskFailureRepository;
  private final ApplicationEventPublisher eventPublisher;

  @Retryable(
      backoff = @Backoff(delay = 1000),
      retryFor = FileException.class
  )
  public boolean uploadFile(User user, UUID id, byte[] bytes) {
    try {

      binaryContentStorage.put(id, bytes);
      return true;
//      return CompletableFuture.completedFuture(true);
    } catch (FileException e) {
      // logging
      throw e;
    }
  }

  @Recover
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public boolean recover(FileException e, User user, UUID id, byte[] bytes) {

    log.info("requestId in recover = {}", MDC.get("requestId"));
    AsyncTaskFailure failure = new AsyncTaskFailure();
    failure.setFields(
        "BinaryContentStorage#put",
        MDC.get("requestId"),
        e.getMessage()
    );

    asyncTaskFailureRepository.saveAndFlush(failure);

    NotificationEvent event = new NotificationEvent(
        List.of(user),
        NotificationType.ASYNC_FAILED,
        null,
        "비동기 작업 실패",
        "파일 업로드에 실패했습니다."
    );

    eventPublisher.publishEvent(event);

    return false;
//    return CompletableFuture.completedFuture(false);
  }
}
