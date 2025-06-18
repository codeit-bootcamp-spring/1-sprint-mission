package com.sprint.mission.discodeit.async.handler;

import com.sprint.mission.discodeit.async.event.AsyncFailedNotificationEvent;
import com.sprint.mission.discodeit.async.event.FileUploadEvent;
import com.sprint.mission.discodeit.async.event.NewMessageNotificationEvent;
import com.sprint.mission.discodeit.async.event.RoleChangedNotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaEventRelayHandler {

  private final KafkaTemplate<String, Object> kafkaTemplate;

  // 파일 업로드 이벤트 중계
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handle(FileUploadEvent event) {
    log.info("🪁 Kafka 전송 - FileUploadEvent: {}", event.getFileId());
    kafkaTemplate.send("discodeit.file-upload-topic", event.getFileId().toString(), event);
  }

  // 새 메시지 등록 이벤트 중계
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handle(NewMessageNotificationEvent event) {
    log.info("🪁 Kafka 전송 - NewMessageNotificationEvent: {}", event.getChannelId());
    kafkaTemplate.send("discodeit.new-message-topic", event.getChannelId().toString(), event);
  }

  // 사용자 역할 변경 이벤트 중계
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handle(RoleChangedNotificationEvent event) {
    log.info("🪁 Kafka 전송 - RoleChangedNotificationEvent: {}", event.getUserId());
    kafkaTemplate.send("discodeit.role-changed-topic", event.getUserId().toString(), event);
  }

  // 비동기 실패 알림 이벤트 중계
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handle(AsyncFailedNotificationEvent event) {
    log.info("🪁 Kafka 전송 - AsyncFailedNotificationEvent: {}", event.getRequestId());
    kafkaTemplate.send("discodeit.async-failed-topic", event.getRequestId().toString(), event);
  }

}
