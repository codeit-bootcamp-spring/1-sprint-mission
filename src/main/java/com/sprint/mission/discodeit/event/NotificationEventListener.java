package com.sprint.mission.discodeit.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.NotificationDto;
import com.sprint.mission.discodeit.entity.NotificationType;
import com.sprint.mission.discodeit.entity.status.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.NotificationService;
import com.sprint.mission.discodeit.service.SseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventListener {

  private final ReadStatusRepository readStatusRepository;
  private final NotificationService notificationService;
  private final ObjectMapper objectMapper;
  private final SseService sseService;

  @Async("eventTaskExecutor")
  @KafkaListener(topics = "discodeit.new_message")
  @Retryable(
      retryFor = {Exception.class},
      noRetryFor = {IllegalArgumentException.class},
      maxAttempts = 3,
      backoff = @Backoff(delay = 1000, multiplier = 2)
  )
  public void handleNewMessageEvent(String kafkaEvent) throws JsonProcessingException {
    log.info("=== Kafka 메시지 수신됨: {}", kafkaEvent);

    NewMessageEvent event = objectMapper.readValue(kafkaEvent, NewMessageEvent.class);

    // NEW_MESSAGE 타입의 경우 알림 설정 확인
    if (event.getType() == NotificationType.NEW_MESSAGE && event.getTargetId() != null) {
      ReadStatus readStatus = readStatusRepository
          .findByChannelIdAndUserId(event.getTargetId(), event.getReceiverId()).orElse(null);
      // 알림이 비활성화되어 있으면 생성하지 않음
      if (readStatus == null || !readStatus.isNotificationEnabled()) {
        log.debug("알림 비활성화 상태: user={}, channel={}", event.getReceiverId(), event.getTargetId());
        return;
      }
    }
    handle(event);
  }

  @Async("eventTaskExecutor")
  @KafkaListener(topics = "discodeit.async_task_failed")
  @Retryable(
      retryFor = {Exception.class},
      noRetryFor = {IllegalArgumentException.class},
      maxAttempts = 3,
      backoff = @Backoff(delay = 1000, multiplier = 2)
  )
  public void handleAsyncTaskFailedEvent(String kafkaEvent) throws JsonProcessingException {
    AsyncTaskFailedEvent event = objectMapper.readValue(kafkaEvent, AsyncTaskFailedEvent.class);
    handle(event);
  }

  @Async("eventTaskExecutor")
  @KafkaListener(topics = "discodeit.role_changed")
  @Retryable(
      retryFor = {Exception.class},
      noRetryFor = {IllegalArgumentException.class},
      maxAttempts = 3,
      backoff = @Backoff(delay = 1000, multiplier = 2)
  )
  public void handleRoleChangedEvent(String kafkaEvent) throws JsonProcessingException {
    UserRoleChangedEvent event = objectMapper.readValue(kafkaEvent, UserRoleChangedEvent.class);
    handle(event);
  }

  private void handle(NotificationEvent event) {
    try {
      log.info("알림 생성 이벤트 발생: {}", event);
      NotificationDto notificationDto = notificationService.create(event);
      log.info("알림 생성 완료: userId = {}", event.getReceiverId());
      sseService.sendNotification(event.getReceiverId(), notificationDto);
    } catch (Exception e) {
      log.error("알림 생성 이벤트 처리 실패: {} (재시도 시작 됨)", event, e);
      throw e; // 재시도를 위해 예외를 다시 던진다
    }
  }
}
