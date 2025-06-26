package com.sprint.mission.discodeit.event.listener;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.AsyncTaskFailure;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.NotificationType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.event.AuthenticatedAsyncTaskFailedEvent;
import com.sprint.mission.discodeit.event.NewMessageEvent;
import com.sprint.mission.discodeit.event.RoleChangedEvent;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.NotificationService;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
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
  private final ChannelService channelService;
  private final ObjectMapper objectMapper;

  @Async("eventTaskExecutor")
  @KafkaListener(topics = "discodeit.new_message")
  @Retryable(
      retryFor = Exception.class,
      maxAttempts = 3,
      backoff = @Backoff(delay = 1000, multiplier = 2)
  )
  public void handleNewMessageEvent(String kafkaEvent) throws JsonProcessingException {
    NewMessageEvent event = objectMapper.readValue(kafkaEvent, NewMessageEvent.class);
    MessageDto messageDto = event.messageDto();
    ChannelDto channelDto = channelService.find(messageDto.channelId());
    log.info("새 메시지 알림 이벤트 처리 시작: channelId={}, messageId={}",
        channelDto.id(), messageDto.id());
    try {
      Set<UUID> receiverIds = readStatusRepository.findAllByChannelIdAndNotificationEnabledTrue(
              channelDto.id())
          .stream()
          .map(ReadStatus::getUser)
          .map(User::getId)
          .filter(id -> !id.equals(messageDto.author().id()))
          .collect(Collectors.toSet());

      UserDto authorDto = messageDto.author();
      String title = channelDto.type().equals(ChannelType.PUBLIC)
          ? String.format("%s (# %s)", authorDto.username(), channelDto.name())
          : authorDto.username();
      String content = messageDto.content();

      notificationService.createAll(
          receiverIds,
          title,
          content,
          NotificationType.NEW_MESSAGE,
          channelDto.id()
      );
      log.info("새 메시지 알림 이벤트 처리 완료 ");
    } catch (Exception e) {
      log.error("새 메시지 알림 이벤트 처리 실패: error={}", e.getMessage(), e);
      throw e;
    }
  }

  @Async("eventTaskExecutor")
  @KafkaListener(topics = "discodeit.role_changed")
  @Retryable(
      retryFor = Exception.class,
      maxAttempts = 3,
      backoff = @Backoff(delay = 1000, multiplier = 2)
  )
  public void handleRoleChangedEvent(String kafkaEvent) throws JsonProcessingException {
    RoleChangedEvent event = objectMapper.readValue(kafkaEvent, RoleChangedEvent.class);
    UUID userId = event.userId();
    Role previousRole = event.previousRole();
    Role newRole = event.newRole();

    log.info("권한 변경 알림 이벤트 처리 시작: userId={}, previousRoleName={}, newRoleName={}",
        userId, previousRole.name(), newRole.name());
    try {
      notificationService.create(
          userId,
          String.format("권한 변경: %s -> %s", previousRole.name(), newRole.name()),
          String.format("관리자에 의해 권한이 '%s'(으)로 변경되었습니다.", newRole.name()),
          NotificationType.ROLE_CHANGED,
          userId
      );
      log.info("권한 변경 알림 이벤트 처리 완료: receiverId={}", userId);
    } catch (Exception e) {
      log.error("권한 변경 알림 이벤트 처리 실패: receiverId={}, error={}", userId, e.getMessage(), e);
      throw e;
    }
  }

  @Async("eventTaskExecutor")
  @KafkaListener(topics = "discodeit.async_task_failed")
  @Retryable(
      retryFor = Exception.class,
      maxAttempts = 3,
      backoff = @Backoff(delay = 1000, multiplier = 2)
  )
  public void handleAsyncTaskFailedEvent(String kafkaEvent) throws JsonProcessingException {
    AuthenticatedAsyncTaskFailedEvent event = objectMapper.readValue(kafkaEvent,
        AuthenticatedAsyncTaskFailedEvent.class);
    AsyncTaskFailure asyncTaskFailure = event.asyncTaskFailedEvent().asyncTaskFailure();
    log.info("비동기 작업 실패 알림 이벤트 처리 시작: requestId={}, taskName={}, failureReason={}",
        asyncTaskFailure.getRequestId(), asyncTaskFailure.getTaskName(),
        asyncTaskFailure.getFailureReason());
    try {
      UUID receiverId = event.authenticatedUserId();
      if (receiverId == null) {
        log.warn("비동기 작업 실패 알림 이벤트 처리 실패: 인증되지 않은 사용자");
        return;
      }
      String title = String.format("비동기 작업 실패: %s", asyncTaskFailure.getTaskName());
      String content = String.format("요청 ID: %s\n실패 사유: %s", asyncTaskFailure.getRequestId(),
          asyncTaskFailure.getFailureReason());

      notificationService.create(
          receiverId,
          title,
          content,
          NotificationType.ASYNC_FAILED,
          null
      );
      log.info("비동기 작업 실패 알림 이벤트 처리 완료: receiverId={}", receiverId);
    } catch (Exception e) {
      log.error("비동기 작업 실패 알림 이벤트 처리 실패: error={}", e.getMessage(), e);
      throw e;
    }
  }
} 