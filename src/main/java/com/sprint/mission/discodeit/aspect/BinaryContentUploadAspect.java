package com.sprint.mission.discodeit.aspect;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.BinaryContentUploadStatus;
import com.sprint.mission.discodeit.event.BinaryContentStatusChangedEvent;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class BinaryContentUploadAspect {

  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentMapper binaryContentMapper;
  private final ApplicationEventPublisher eventPublisher;

  private static final ThreadLocal<UUID> currentUserId = new ThreadLocal<>();

  public static void setCurrentUserId(UUID userId) {
    currentUserId.set(userId);
  }

  public static void clearCurrentUserId() {
    currentUserId.remove();
  }

  @AfterReturning("execution(* com.sprint.mission.discodeit.repository.BinaryContentRepository.updateUploadStatus(..)) && args(binaryContentId, status)")
  public void afterUpdateUploadStatus(UUID binaryContentId, BinaryContentUploadStatus status) {
    try {
      UUID userId = currentUserId.get();
      if (userId == null) {
        log.warn("현재 사용자 ID를 찾을 수 없습니다. SSE 이벤트를 발행하지 않습니다.");
        return;
      }

      BinaryContent binaryContent = binaryContentRepository.findById(binaryContentId)
          .orElse(null);

      if (binaryContent != null) {
        BinaryContentDto dto = binaryContentMapper.toDto(binaryContent);

        eventPublisher.publishEvent(new BinaryContentStatusChangedEvent(dto, userId));

        log.info("파일 업로드 상태 변경 SSE 이벤트 발행: binaryContentId={}, status={}, userId={}",
            binaryContentId, status, userId);
      }
    } catch (Exception e) {
      log.error("파일 업로드 상태 변경 이벤트 발행 실패: binaryContentId={}", binaryContentId, e);
    }
  }
}