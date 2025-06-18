package com.sprint.mission.discodeit.event;


import com.sprint.mission.discodeit.async.binary_content.BinaryContentStorageWrapperService;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.UploadStatus;
import com.sprint.mission.discodeit.event.event_entity.BinaryContentUploadEvent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Component
@RequiredArgsConstructor
public class BinaryContentUploadListener {

  private final BinaryContentStorageWrapperService binaryContentStorageWrapperService;
  private final BinaryContentService binaryContentService;

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handleUpload(BinaryContentUploadEvent event) {

    List<BinaryContent> contents = event.getContents();
    List<MultipartFile> files = event.getFiles();

    for (int i = 0; i < contents.size(); i++) {
      BinaryContent content = contents.get(i);
      MultipartFile file = files.get(i);

      try {
        CompletableFuture<Boolean> future = binaryContentStorageWrapperService.uploadFile(
            event.getUser(),
            content.getId(), file.getBytes());

        future.whenComplete((success, ex) -> {
          if (ex != null) {
            content.changeUploadStatus(UploadStatus.FAILED);
            log.warn("[ATTACHMENT UPLOAD FAILED - EXCEPTION] : [CONTENT_ID: {}]", content.getId());
          } else if (success) {
            content.changeUploadStatus(UploadStatus.SUCCESS);
            log.info("[ATTACHMENT UPLOAD SUCCESS] : [CONTENT_ID: {}]", content.getId());
          } else {
            content.changeUploadStatus(UploadStatus.FAILED);
            log.warn("[ATTACHMENT UPLOAD FAILED - RECOVER] : [CONTENT_ID: {}]", content.getId());
          }

          binaryContentService.update(content);
        });

      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
