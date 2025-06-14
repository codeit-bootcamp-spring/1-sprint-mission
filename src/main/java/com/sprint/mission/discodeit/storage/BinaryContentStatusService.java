package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.entity.BinaryContentUploadStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BinaryContentStatusService {

  private final BinaryContentRepository binaryContentRepository;

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void updateBinaryStatus(UUID binaryContentId, BinaryContentUploadStatus status) {
    binaryContentRepository.findById(binaryContentId)
      .ifPresent(binaryContent -> {
        binaryContent.updateUploadSuccess(status);
        log.info("바이너리 컨텐츠 업로드 상태 업데이트: id={}, status={}", binaryContent.getId(), status);
      });
  }
}
