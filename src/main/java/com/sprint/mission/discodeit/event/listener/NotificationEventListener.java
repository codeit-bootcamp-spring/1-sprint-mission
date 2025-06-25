package com.sprint.mission.discodeit.event.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.AsyncTaskFailure;
import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.event.AuthenticatedAsyncTaskFailedEvent;
import com.sprint.mission.discodeit.event.NewMessageEvent;
import com.sprint.mission.discodeit.event.RoleChangedEvent;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.NotificationService;
import java.util.UUID;
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

  private final NotificationService notificationService;
  private final ReadStatusRepository readStatusRepository;
  private final ObjectMapper objectMapper;

  @Async("eventExecutor")
  @KafkaListener(topics = "discodeit.new_message")
  @Retryable(
      retryFor = Exception.class,
      backoff = @Backoff(delay = 2000)
  )
  public void handleNewMessageEvent(String kafkaEvent) throws JsonProcessingException {
    log.info("새 메세지 생성 이벤트 발생");
    NewMessageEvent event = objectMapper.readValue(kafkaEvent, NewMessageEvent.class);
    MessageDto messageDto = event.messageDto();
    ChannelDto channelDto = event.channelDto();

    UserDto authorDto = messageDto.author();
    String title = channelDto.type().equals(Channel.Type.PUBLIC)
        ? String.format("%s (# %s)", authorDto.username(), channelDto.name())
        : authorDto.username();
    String content = messageDto.content();

    readStatusRepository.findByChannelIdAndNotificationEnabledTure(messageDto.channelId()).stream()
        .map(readStatus -> readStatus.getUser().getId())
        .filter(userId -> !userId.equals(messageDto.author().id()))
        .forEach(id -> notificationService.create(id, title, content, event.type(), channelDto.id()));
  }

  @Async("eventExecutor")
  @KafkaListener(topics = "discodeit.role_changed")
  @Retryable(
      retryFor = Exception.class,
      backoff = @Backoff(delay = 2000)
  )
  public void handleRoleChangedEvent(String kafkaEvent) throws JsonProcessingException {
    log.info("역할 변경 이벤트 발생");
    RoleChangedEvent event = objectMapper.readValue(kafkaEvent, RoleChangedEvent.class);
    String title = String.format("권한 변경: %s -> %s", event.oldRole(), event.newRole());
    String content = String.format("관리자에 의해 권한이 '%s'(으)로 변경되었습니다.", event.newRole());
    notificationService.create(
        event.userId(),
        title,
        content,
        event.type(),
        event.userId()
    );
  }

  @Async("eventExecutor")
  @KafkaListener(topics = "discodeit.async_task_failed")
  @Retryable(
      retryFor = Exception.class,
      backoff = @Backoff(delay = 2000)
  )
  public void handleAuthenticatedAsyncTaskFailedEvent(String kafkaEvent) throws JsonProcessingException {
    log.info("비동기 파일 업로드 실패 이벤트 발생");
    AuthenticatedAsyncTaskFailedEvent event = objectMapper.readValue(kafkaEvent, AuthenticatedAsyncTaskFailedEvent.class);
    AsyncTaskFailure asyncTaskFailure = event.asyncTaskFailedEvent().asyncTaskFailure();

    UUID id = event.authenticatedUserId();
    if (id == null) {
      log.warn("비동기 작업 실패 알림 이벤트 처리 실패: 인증되지 않은 사용자");
      return;
    }

    String title = String.format("비동기 작업 실패: %s", asyncTaskFailure.taskName());
    String content = String.format("요청 ID: %s\n실패 사유: %s", asyncTaskFailure.requestId(),
        asyncTaskFailure.failureReason());

    notificationService.create(id, title, content, event.asyncTaskFailedEvent().type(), null);
  }
}
