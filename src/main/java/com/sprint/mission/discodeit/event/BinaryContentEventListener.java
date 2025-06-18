package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.entity.status.BinaryContentUploadStatus;
import com.sprint.mission.discodeit.service.BinaryContentStatusService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;


@Slf4j
@Component
@RequiredArgsConstructor
public class BinaryContentEventListener {

  private final BinaryContentStorage binaryContentStorage;
  private final BinaryContentStatusService binaryContentStatusService;

  @Async
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handleBinaryContentCreated(BinaryContentCreatedEvent event) {
    CompletableFuture<UUID> uploadFuture = binaryContentStorage.asyncPut(
        event.binaryContentId(),
        event.fileData()
    );

    uploadFuture
        .thenAccept(uploadedId -> {
          log.info("비동기 파일 저장 성공");
          binaryContentStatusService.updateStatus(uploadedId,
              BinaryContentUploadStatus.SUCCESS);

          log.info("파일 상태 업데이트 완료: uploadedId = {}", uploadedId);
        })
        .exceptionally(throwable -> {
          log.warn("비동기 파일 저장 실패");
          binaryContentStatusService.updateStatus(event.binaryContentId(),
              BinaryContentUploadStatus.FAILED);
          return null;
        });
  }
}
