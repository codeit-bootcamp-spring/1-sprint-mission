package com.sprint.mission.discodeit.service.integration;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.BinaryContentUploadStatus;
import com.sprint.mission.discodeit.event.BinaryContentStatusChangedEvent;
import com.sprint.mission.discodeit.event.notification.NotificationEvent;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class BinaryContentStatusIntegration {

  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentMapper binaryContentMapper;
  private final ApplicationEventPublisher eventPublisher;

  @Async
  @Transactional
  public void updateStatusAndPublishEvent(UUID binaryContentId,
      BinaryContentUploadStatus status,
      UUID userId) {
    try {
      binaryContentRepository.updateUploadStatus(binaryContentId, status);

      BinaryContent binaryContent = binaryContentRepository.findById(binaryContentId)
          .orElseThrow(
              () -> new IllegalArgumentException("BinaryContent not found: " + binaryContentId));

      BinaryContentDto dto = binaryContentMapper.toDto(binaryContent);

      eventPublisher.publishEvent(new BinaryContentStatusChangedEvent(dto, userId));

      if (status == BinaryContentUploadStatus.FAILED) {
        NotificationEvent notificationEvent = NotificationEvent.asyncFailed(
            userId,
            String.format("파일 업로드 실패: %s", binaryContent.getFileName())
        );
        eventPublisher.publishEvent(notificationEvent);
      }

      log.info("파일 업로드 상태 변경 이벤트 발행: binaryContentId={}, status={}, userId={}",
          binaryContentId, status, userId);

    } catch (Exception e) {
      log.error("파일 업로드 상태 업데이트 실패: binaryContentId={}", binaryContentId, e);
    }
  }
}