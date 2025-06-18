package com.sprint.mission.discodeit.handle;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.NotificatonDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
@RequiredArgsConstructor
public class NotificationEventHandler {

  private final KafkaTemplate<String, String> kafkaTemplate;
  private final ObjectMapper objectMapper;

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handleEvent(NotificatonDto event) {
    try {
      String message = objectMapper.writeValueAsString(event);
      kafkaTemplate.send("discodeit.notification", message);
    } catch (JsonProcessingException e) {
      log.error("Kafka 직렬화 실패", e);
    }
  }

//  private final AsyncNotificationSender asyncNotificationSender;
//
//  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
//  public void handleEvent(NotificatonDto event) {
//    asyncNotificationSender.send(event);
//  }


}