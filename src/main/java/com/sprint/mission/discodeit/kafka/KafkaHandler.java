package com.sprint.mission.discodeit.kafka;

import com.sprint.mission.discodeit.events.AsyncFailedEvent;
import com.sprint.mission.discodeit.events.ChangeRoleEvent;
import com.sprint.mission.discodeit.events.NotiMessageEvent;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaHandler {

  private static final String NOTIFICATION = "discodeit.new-message";
  private static final String CHANGE_ROLE = "discodeit.role-changed";
  private static final String ASYNC_FAILED = "discodeit.async-task-failed";

  private final KafkaTemplate<String, Object> kafkaTemplate;
  private final UserRepository userRepository;
  private final ReadStatusRepository readStatusRepository;
  private final NotificationRepository notificationRepository;

  /**
   * @methodName : handle
   * @date : 2025. 6. 5. 16:16
   * @author : wongil
   * @Description: 메세지 알림
   **/
  @Retryable(retryFor = UserNotFoundException.class, maxAttempts = 5, backoff = @Backoff(delay = 1500))
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handle(NotiMessageEvent event) {
    log.info("[Kafka Message Event 알림] sender:{}, channel:{}, content:{}", event.getSenderId(),
        event.getChannelId(), event.getContent());

    kafkaTemplate.send(NOTIFICATION, event.getSenderId().toString(), event);
  }

  /**
   * @methodName : handle
   * @date : 2025. 6. 5. 16:16
   * @author : wongil
   * @Description: 유저 역할 변경
   **/
  public void handle(ChangeRoleEvent event) {
    log.info("[Kafka Change Role Event 알림] user:{}, preRole:{}, currentRole:{}", event.getUserId(),
        event.getPreRole(), event.getCurrentRole());

    kafkaTemplate.send(CHANGE_ROLE, event.getUserId().toString(), event);
  }

  /**
   * @methodName : handle
   * @date : 2025. 6. 5. 16:19
   * @author : wongil
   * @Description: 비동기 에러 알림 핸들러
   **/
  public void handle(AsyncFailedEvent event) {
    log.info("[Kafka Async Fail Event 알림] request:{}, file:{}, message:{}", event.getRequestId(),
        event.getFileId(), event.getMessage());

    kafkaTemplate.send(ASYNC_FAILED, event.getRequestId().toString(), event);
  }

}
