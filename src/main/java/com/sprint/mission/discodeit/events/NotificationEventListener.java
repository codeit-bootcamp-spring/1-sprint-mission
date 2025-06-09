package com.sprint.mission.discodeit.events;

import com.sprint.mission.discodeit.entity.notification.Notification;
import com.sprint.mission.discodeit.entity.notification.NotificationType;
import com.sprint.mission.discodeit.entity.status.read.ReadStatus;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventListener {

  private static final String NOTI = "discodeit.new-message";
  private static final String ROLE_CHANGE = "discodeit.role-changed";
  private static final String ASYNC_FAILED = "discodeit.async-task-failed";
  private static final String GROUP_ID = "discodeit-group";

  private final UserRepository userRepository;
  private final NotificationRepository notificationRepository;
  private final ReadStatusRepository readStatusRepository;

  /**
   * @methodName : handle
   * @date : 2025. 6. 5. 09:35
   * @author : wongil
   * @Description: 사용자가 알림을 활성화한 채팅방에 메세지가 등록된 경우
   **/
  @Async("notificationExecutor")
  @KafkaListener(topics = NOTI, groupId = GROUP_ID)
  @Retryable(retryFor = UserNotFoundException.class, maxAttempts = 5, backoff = @Backoff(delay = 1500))
  public void handle(@Payload NotiMessageEvent event) {

    log.info("[Message Event 알림] sender:{}, channel:{}, content:{}", event.getSenderId(),
        event.getChannelId(), event.getContent());

    List<ReadStatus> notiEnableReadStatus = readStatusRepository.findAllByChannel_IdAndNotificationEnabled(
        event.getChannelId(), true);
    List<UUID> enabledUser = notiEnableReadStatus.stream()
        .map(readStatus -> readStatus.getUser().getId())
        .filter(userId -> !userId.equals(event.getSenderId()))
        .toList();

    enabledUser.forEach(userId -> {

      User receiver = getUser(userId);
      User sender = getUser(event.getSenderId());

      Notification notification = Notification.builder()
          .receiver(receiver)
          .type(NotificationType.NEW_MESSAGE)
          .title(sender.getUsername())
          .content(event.getContent())
          .targetId(event.getChannelId())
          .build();

      notificationRepository.save(notification);
    });
  }

  /**
   * @methodName : handle
   * @date : 2025. 6. 5. 09:36
   * @author : wongil
   * @Description: 사용자 권한이 변경 된 경우 이벤트 리스너
   **/
  @Async("notificationExecutor")
  @KafkaListener(topics = ROLE_CHANGE, groupId = GROUP_ID)
  @Retryable(retryFor = UserNotFoundException.class, backoff = @Backoff(delay = 3000))
  public void handle(ChangeRoleEvent event) {
    log.info("[Change Role Event 알림] user:{}, preRole:{}, currentRole:{}", event.getUserId(),
        event.getPreRole(), event.getCurrentRole());

    User receiver = getUser(event.getUserId());

    Notification notification = Notification.builder()
        .receiver(receiver)
        .type(NotificationType.ROLE_CHANGED)
        .title("권한이 변경되었습니다.")
        .content("[" + event.getCurrentRole() + "] -> [" + event.getPreRole() + "]")
        .targetId(event.getUserId())
        .build();

    notificationRepository.save(notification);
  }

  /**
   * @methodName : handle
   * @date : 2025. 6. 5. 09:44
   * @author : wongil
   * @Description: 비동기 작업 실패
   **/
  @Async("notificationExecutor")
  @KafkaListener(topics = ASYNC_FAILED, groupId = GROUP_ID)
  @Retryable(retryFor = Exception.class, maxAttempts = 5, backoff = @Backoff(delay = 3000))
  public void handle(AsyncFailedEvent event) {
    log.info("[비동기 작업 실패 Event 알림] file:{}, message:{}", event.getFileId(), event.getMessage());

    Notification notification = Notification.builder()
        .receiver(null)
        .type(NotificationType.ASYNC_FAILED)
        .title("백그라운드 작업이 실패했습니다.")
        .content(event.getMessage())
        .targetId(event.getRequestId())
        .build();

    notificationRepository.save(notification);
  }

  private User getUser(UUID userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(Instant.now(), ErrorCode.USER_NOT_FOUND,
            Map.of(
                ErrorCode.USER_NOT_FOUND.getCode(),
                ErrorCode.USER_NOT_FOUND.getMessage()
            )));
  }
}
