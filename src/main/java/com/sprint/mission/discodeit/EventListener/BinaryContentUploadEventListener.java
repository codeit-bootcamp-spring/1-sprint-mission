package com.sprint.mission.discodeit.EventListener;

import com.sprint.mission.discodeit.dto.response.BinaryContentUploadEvent;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class BinaryContentUploadEventListener {

  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentStorage binaryContentStorage;

  @Async
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handleUpload(BinaryContentUploadEvent event) {
    UUID id = event.binaryContentId();
    byte[] bytes = event.bytes();

    log.debug("트랜잭션 커밋 후 비동기 업로드 시작: id={}", id);

    binaryContentStorage.put(id, bytes)
        .thenRun(() -> {
          BinaryContent content = binaryContentRepository.findById(id).orElseThrow();
          content.markSuccess();
          binaryContentRepository.save(content);
          log.info("업로드 성공: id={}", id);
        })
        .exceptionally(ex -> {
          BinaryContent content = binaryContentRepository.findById(id).orElseThrow();
          content.markFailed();
          binaryContentRepository.save(content);
          log.error("업로드 실패: id={}, err={}", id, ex.getMessage());
          return null;
        });
  }
}
