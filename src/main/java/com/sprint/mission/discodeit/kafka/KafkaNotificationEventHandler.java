package com.sprint.mission.discodeit.kafka;

import com.sprint.mission.discodeit.event.NotificationCreateEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaNotificationEventHandler {

  private final KafkaTemplate<String, NotificationCreateEvent> kafkaTemplate;

  private static final String TOPIC = "notification-events";

  @TransactionalEventListener
  public void handle(NotificationCreateEvent event) {
    log.info("Kafka 전송 시작: {}", event);
    kafkaTemplate.send(TOPIC, event)
        .whenComplete((result, ex) -> {
          if (ex != null) {
            log.error("Kafka 전송 실패", ex);
          } else {
            log.info("Kafka 전송 완료: {}", result.getRecordMetadata());
          }
        });
  }
}
